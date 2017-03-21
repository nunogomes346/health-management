package ejbs;

import dtos.FaqDTO;
import entities.Caregiver;
import entities.Counter;
import entities.FAQ;
import entities.Need;
import exceptions.EntityDoesNotExistException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class FaqBean {

    @PersistenceContext
    EntityManager em;

    public void create(String description, String question, String answer)
            throws MyConstraintViolationException {
        try {
            FAQ faq = new FAQ(description, question, answer);

            em.persist(faq);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void update(int id, String description, String question, String answer) 
            throws EntityDoesNotExistException, MyConstraintViolationException {
        try {
            FAQ faq = em.find(FAQ.class, id);
            if (faq == null) {
                throw new EntityDoesNotExistException("There is no FAQ with that id.");
            }

            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersResource")
                    .setParameter("resource", faq.getDescription())
                    .getResultList();
            for (Counter counter : allCounters) {
                counter.setResource(description);
                em.merge(counter);                
            }
            
            faq.setDescription(description);
            faq.setQuestion(question);
            faq.setAnswer(answer);

            em.merge(faq);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<FaqDTO> getAll() {
        try {
            List<FAQ> faqs = (List<FAQ>) em.createNamedQuery("getAllFAQs").getResultList();

            return faqsToDTOs(faqs);
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public FaqDTO getFaq(int id) throws EntityDoesNotExistException {
        try {
            FAQ faq = em.find(FAQ.class, id);
            if (faq == null) {
                throw new EntityDoesNotExistException("There is no FAQ with that id.");
            }
            
            return faqToDTO(faq);
        } catch (EntityDoesNotExistException e) {
            throw e;
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void remove(int id) throws EntityDoesNotExistException {
        try {
            FAQ faq = em.find(FAQ.class, id);
            if (faq == null) {
                throw new EntityDoesNotExistException("There is no FAQ with that id.");
            }

            for (Caregiver caregiver : faq.getCaregivers()) {
                caregiver.removeMaterial(faq);
            }
            
            for (Need need : faq.getNeeds()) {
                need.removeMaterial(faq);
            }
            
            List<Counter> allCounters = (List<Counter>) em.createNamedQuery("getAllCountersResource")
                    .setParameter("resource", faq.getDescription())
                    .getResultList();
            for (Counter counter : allCounters) {
                em.remove(counter);
            }
            
            em.remove(faq);
        } catch (EntityDoesNotExistException e) {
            throw e;    
        } catch (EJBException e) {
            throw new EJBException(e.getMessage());
        }
    }

    //Build DTOs
    public FaqDTO faqToDTO(FAQ faq) {
        return new FaqDTO(
                faq.getId(),
                faq.getDescription(),
                faq.getQuestion(),
                faq.getAnswer()
        );
    }

    public List<FaqDTO> faqsToDTOs(List<FAQ> faqs) {
        List<FaqDTO> dtos = new ArrayList<>();
        for (FAQ f : faqs) {
            dtos.add(faqToDTO(f));
        }
        return dtos;
    }

}
