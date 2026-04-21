package com.hana8.hanaro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hana8.hanaro.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	boolean existsByEmail(String email);
}
