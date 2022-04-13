package com.sanha.po_education;

import com.sanha.po_education.entity.UserEntity;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

@SpringBootApplication
public class HelloJpaApplication {

	public static void main(String[] args) {

		// EntityManagerFactory는 EntityManager를 생성하기 위한 팩토리 인터페이스로 persistence 단위별로 팩토리를 생성
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
				"hello"); // persistence.xml 파일에 기입한 이름을 적어줘야 함

		System.out.println("Check 1"); 

		// EntityManager 객체를 생성
		// EntityManager 는 Persistence Context와 Entity를 관리, 하나의 커넥션/세션으로 보면 됨
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		System.out.println("Check 2");

		// EntityManager에서 트랜잭션을 가져와 관리하기 위한 객체 생성
		EntityTransaction entityTransaction = entityManager.getTransaction();

		System.out.println("Check 3");

		try {
			// 트랜잭션을 시작해야 DB를 조작할 수 있음
			entityTransaction.begin();

			System.out.println("Check 4");

			// 저장하고자 하는 엔티티 객체를 생성
			UserEntity userEntity = new UserEntity("aaa@aaa.com", "name",
					LocalDateTime.now(), LocalDateTime.now());

			System.out.println("Check 5");
			// UserEntity 객체를 Persistence Context에 추가
			entityManager.persist(userEntity);

			UserEntity user1 = entityManager.find(UserEntity.class, "aaa@aaa.com");
			UserEntity user2 = entityManager.find(UserEntity.class, "aaa@aaa.com");

			// 엔티티 동일성 보장
			System.out.println("User : " + (user1 == user2));


			System.out.println("Check 6");

			userEntity.setName("modifyName");

			// 따로 Update 작업을 하지 않아도 위에 객체가 수정되었기 때문에, 변경 감지
			//entityTransaction.update();

			// 실제 DB에 반영
			entityTransaction.commit();

			System.out.println("Check 7");



		} catch (Exception e) {
			e.printStackTrace();

			// 예외가 발생했을 경우 트랜잭션 롤백 진행
			entityTransaction.rollback();

		} finally {

			// 엔티티 매니저 종료. JDBC에서 Connection 종료하는 것과 동일한 기능으로 보면 됨
			entityManager.close();
		}

		System.out.println("Check 8");

		// 팩토리 종료. 커넥션 풀 자원을 반환
		entityManagerFactory.close();
	}

}
