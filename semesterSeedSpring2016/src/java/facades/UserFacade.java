package facades;

import entity.Airline;
import entity.Passenger;
import entity.Reservation;
import entity.Role;
import security.IUserFacade;
import entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import openshift_deploy.DeploymentConfiguration;
import security.IUser;
import security.PasswordStorage;

public class UserFacade implements IUserFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory(DeploymentConfiguration.PU_NAME);

    public UserFacade() {

    }

    @Override
    public IUser getUserByUserId(String id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }
    /*
     Return the Roles if users could be authenticated, otherwise null
     */

    @Override
    /*
     Return the Roles if users could be authenticated, otherwise null
     */
    public List<String> authenticateUser(String userName, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            User user = em.find(User.class, userName);
            if (user == null) {
                return null;
            }

            boolean authenticated;
            try {
                authenticated = PasswordStorage.verifyPassword(password, user.getPassword());
                return authenticated ? user.getRolesAsStrings() : null;
            } catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException ex) {
                Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } finally {
            em.close();
        }
    }

    public User addPerson(User u) throws PasswordStorage.CannotPerformOperationException {
        EntityManager em = emf.createEntityManager();

        u.AddRole(em.find(Role.class, "User"));

        try {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
            return u;
        } finally {
            em.close();
        }
    }

    public Reservation saveReservation(Reservation r) {
        EntityManager em = emf.createEntityManager();

        List<Passenger> passengers = r.getPassengers();
        for (Passenger p : passengers) {
            p.setReservation(r);
        }
        r.setUser(em.find(User.class, r.getUser().getUserName()));
        try {
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
            return r;
        } finally {
            em.close();
        }
    }

    public List<entity.User> getUsers() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select u from User u").getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Reservation> getReservations() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select r from Reservation r").getResultList();
        } finally {
            em.close();
        }
    }

    public List<Reservation> getUserReservation(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select r from Reservation r where r.user.username = :username").setParameter("username", username).getResultList();
        } finally {
            em.close();
        }

    }

    public Object getAirlineURL(String airlineName) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select u from Airline u where u.airlineName = :airlineName").setParameter("airlineName", airlineName).getSingleResult();
        } finally {
            em.close();
        }

    }

    public List<Airline> getAllAirlineURL() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select u from Airline u").getResultList();
        } finally {
            em.close();
        }

    }

    public ArrayList<String> getAllAirlineURLString() {
        EntityManager em = emf.createEntityManager();
        ArrayList<String> result = new ArrayList();
        try {
            List<Airline> airlines = em.createQuery("select u from Airline u", Airline.class).getResultList();
            for (Airline a : airlines) {
                result.add(a.getUrl());
            }

        } finally {
            em.close();
        }
        return result;
    }

    public entity.User deleteUser(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            entity.User u = em.find(entity.User.class, username);
            em.getTransaction().begin();
            //em.createQuery("delete from User u where u.userName = :username").setParameter("username", username).executeUpdate();
            em.remove(u);
            em.getTransaction().commit();

            return u;
        } finally {
            em.close();
        }
    }
    
    public Reservation deleteReservation(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Reservation r = em.find(Reservation.class, id);
            em.getTransaction().begin();
            //em.createQuery("delete from User u where u.userName = :username").setParameter("username", username).executeUpdate();
            em.remove(r);
            em.getTransaction().commit();

            return r;
        } finally {
            em.close();
        }
    }
}
