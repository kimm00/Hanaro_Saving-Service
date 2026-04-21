package com.hana8.hanaro.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 원본 파일명
	@Column(nullable = false, length = 100)
	private String orgName;

	// 저장 파일명 (UUID 등)
	@Column(nullable = false, length = 100)
	private String saveName;

	// 저장 경로
	@Column(nullable = false, length = 200)
	private String saveDir;

	// 설명 (선택)
	@Column(length = 1000)
	private String remark;

	// 썸네일 여부
	private boolean isThumbnail;

	// 상품 연관관계
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Product product;

	@Transient
	public String getImageUrl() {

		return "/upload/" + this.saveDir + "/" + this.saveName;
	}
}
