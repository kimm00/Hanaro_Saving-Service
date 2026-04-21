package com.hana8.hanaro.entity;

import java.time.LocalDate;

import com.hana8.hanaro.common.enums.AccountStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 계좌번호
	@Column(unique = true, nullable = false, length = 20)
	private String accountNumber;

	// 회원
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	// 잔액
	private Long balance;

	// 계좌 상태
	@Enumerated(EnumType.STRING)
	private AccountStatus status;

	// 가입일
	private LocalDate startDate;

	@OneToOne(mappedBy = "account")
	private Subscription subscription;

}
