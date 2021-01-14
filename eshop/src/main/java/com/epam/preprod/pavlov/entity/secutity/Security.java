package com.epam.preprod.pavlov.entity.secutity;

import com.epam.preprod.pavlov.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.List;

@XmlType(name = "security")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Security {

    private static final Logger SECURITY_LOGGER = LoggerFactory.getLogger(Security.class);
    @XmlElement(name = "constraint")
    private List<Constraint> constraints;

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public static Security forFile(File file) {
        SECURITY_LOGGER.info("Parsing security xml file...");
        if (!file.exists()) {
            return null;
        }
        Security security;
        try {
            JAXBContext context = JAXBContext.newInstance(Security.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            security = (Security) unmarshaller.unmarshal(file);
        } catch (JAXBException ex) {
            SECURITY_LOGGER.error("JaxbException occurred during parsing xml security file");
            throw new ApplicationException("JaxException in Security class");
        }
        return security;
    }
}
















