package br.com.erudio.repositories;

import br.com.erudio.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User WHERE u.UserName = :UserName")
    User findByUsername(@Param("UserName") String UserName);
}
