package com.qr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qr.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByQrToken(String qrToken);
}

