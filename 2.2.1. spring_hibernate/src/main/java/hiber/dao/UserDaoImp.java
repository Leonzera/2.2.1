package hiber.dao;

import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public void deleteUserById(long id) {
      Query query = sessionFactory.getCurrentSession().createQuery("delete from User u where u.id = :userId");
      query.setParameter("userId", id);
      query.executeUpdate();
   }

   @Override
   public Optional<User> getUserByCar(String model, int series) {
        List <User> user = sessionFactory.getCurrentSession()
                 .createQuery(
                         "FROM User u WHERE u.car.model = :model AND u.car.series = :series",
                         User.class
                 )
                 .setParameter("model", model)
                 .setParameter("series", series)
                 .getResultList();
      if (user.isEmpty()) {
         return Optional.empty();
      }

      if (user.size() > 1) {
         System.err.println("Найдено  " + user.size() + " владельцев " + model + " " + series);
      }
         return Optional.of(user.get(0));


   }
}
