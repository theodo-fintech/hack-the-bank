package com.sipios.bank.controller;

import com.sipios.bank.model.Transfer;
import com.sipios.bank.model.User;
import com.sipios.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.Properties;

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
        model.addAttribute("transfer", new Transfer());
        model.addAttribute("user", user);

        return "transfert";
    }

    @PostMapping("/user/{userId}/virement")
    public ResponseEntity<?> transferSubmit(@RequestBody String transferBody, @PathVariable Long userId) throws JAXBException, XMLStreamException {
        JAXBContext jc = JAXBContext.newInstance(Transfer.class);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, true);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(new StringReader(transferBody)));

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Transfer transfer = (Transfer) unmarshaller.unmarshal(xsr);

        String message;
        if (transfer.getAmount() > 2000 && transfer.getCode() != null && transfer.getCode().equals("MONCHIENSAPPELLEEUGENE")) {
            message = String.format("Le virement de montant %s€ à destination de %s a été accepté", transfer.getAmount(), transfer.getTargetIban());
            // TO DO : Notify us that we have a winner :)
        } else if (transfer.getAmount() > 2000 && transfer.getCode() != null && !transfer.getCode().isEmpty()) {
           message = "Le code secret entré n'est pas valide";
        } else if (transfer.getAmount() > 2000) {
            message = "Le virement n'est pas autorisé (montant > 2000€)";
        } else {
            message = String.format("Le virement de montant %s€ à destination de %s a été accepté", transfer.getAmount(), transfer.getTargetIban());
        }

        return ResponseEntity.ok(message);
    }
}
