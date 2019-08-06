package com.sipios.bank.controller;

import com.sipios.bank.model.Transfer;
import com.sipios.bank.model.User;
import com.sipios.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

@Controller
public class TransferController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}/virement")
    public String transfert(
            Model model,
            @PathVariable Long userId
    ) {
        User user = userRepository.getOne(userId);
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_USER_SUPER_PREMIUM"))) {
            model.addAttribute("transfer", new Transfer());
        }

        model.addAttribute("user", user);

        return "transfert";
    }

    @PostMapping("/user/{userId}/virement")
    public ResponseEntity<String> transferSubmit(@RequestBody String transferBody, @PathVariable Long userId) throws JAXBException, XMLStreamException {
        if (userRepository.getOne(userId).getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_USER_SUPER_PREMIUM"))) {
            return ResponseEntity.status(403).body("Vous n'êtes pas un utilisateur super prémium");
        }
        
        JAXBContext jc = JAXBContext.newInstance(Transfer.class);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, true);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(new StringReader(transferBody)));

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Transfer transfer = (Transfer) unmarshaller.unmarshal(xsr);

        String validPin = "4808";

        // TODO : Notify us that we have a winner :)
        if (transfer.getAmount() > 2000 && transfer.getCode() != null && transfer.getCode().equals("MONCHIENSAPPELLEEUGENE") && transfer.getPinCode() != null && transfer.getPinCode().equals(validPin)) {
            return ResponseEntity.ok(
                String.format(
                    "Félicitation ! Le virement de montant %s€ à destination de %s a été accepté.\n Vous venez de réussir le Dojo!\n Merci d'avoir participé. N'hesitez pas à vous vanter de votre victoire.",
                    transfer.getAmount(),
                    transfer.getTargetIban()
                )
            );
        }

        if (transfer.getAmount() <= 2000 && (transfer.getCode() == null || transfer.getCode().isEmpty()) && transfer.getPinCode() != null && transfer.getPinCode().equals(validPin)) {
            return ResponseEntity.ok(
                String.format(
                    "Le virement de montant %s€ à destination de %s a été accepté.\n Bravo! Vous venez de passer à l'étape 8.",
                    transfer.getAmount(),
                    transfer.getTargetIban()
                )
            );
        }

        String errorMessage = "";
        if (transfer.getAmount() > 2000 && transfer.getCode() != null && !transfer.getCode().isEmpty()) {
            errorMessage = "Le code secret entré n'est pas valide (Pour rappel, le code se trouve sur le serveur dans le dossier /home/admin/code.txt)\n";
        } else if (transfer.getAmount() > 2000) {
            errorMessage = "Le virement n'est pas autorisé (montant > 2000€)\n";
        }

        if (transfer.getPinCode().isEmpty()) {
            errorMessage += "Votre code personnel es obligatoire\n";
        } else if (transfer.getPinCode().length() > 4) {
            errorMessage += "Le code pin entré est trop grand\n";
        }else if (transfer.getPinCode().length() < 4) {
            errorMessage += "Le code pin entré est trop petit\n";
        }

        return ResponseEntity.ok(errorMessage);
    }
}
