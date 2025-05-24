package com.example.motogp_b.repository;

import com.example.motogp_b.dto.UserDto;
import com.example.motogp_b.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("""
            SELECT u FROM User u
            WHERE
                ( :#{#userDto.keyword} IS NULL OR :#{#userDto.keyword} = '' OR
                  u.name LIKE %:#{#userDto.keyword}% OR
                  u.email LIKE %:#{#userDto.keyword}% OR
                  u.nickname LIKE %:#{#userDto.keyword}% )
            AND
                ( :#{#userDto.role} IS NULL OR :#{#userDto.role} = '' OR
                  u.role = :#{#userDto.role} )
            """)
    List<User> findAllUsers(@Param("userDto") UserDto userDto);

//    User findByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByOtp(String otp);
}