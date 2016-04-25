package facades;

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
    
    public List<entity.User> getUsers() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select u from User u").getResultList();
        } finally {
            em.close();
        }
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
}
