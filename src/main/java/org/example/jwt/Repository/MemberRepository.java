package org.example.jwt.Repository;

import org.example.jwt.Model.DAO.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsernameAndPassword(String username, String password);
}
