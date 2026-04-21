-- FK 끄기
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM Subscription;
DELETE FROM ProductImage;
DELETE FROM Account;
DELETE FROM Product;
DELETE FROM Member;

SET FOREIGN_KEY_CHECKS = 1;

SET time_zone = '+09:00';

-- Member (password: 12345678)
INSERT INTO Member (id, email, password, nickname, role, createdAt, updatedAt)
VALUES
    (1, 'new@test.com', '$2a$10$72uGn8Ji1jwUYa6aP7Vh1eUGZ97Xsd/v6G0itWOGLMjCY2gI54Da6', 'test', 'ROLE_USER', NOW(), NOW()),
    (2, 'admin@email.com', '$2a$10$72uGn8Ji1jwUYa6aP7Vh1eUGZ97Xsd/v6G0itWOGLMjCY2gI54Da6', 'admin', 'ROLE_ADMIN', NOW(), NOW()),
    (3, 'hanaro@email.com', '$2a$10$72uGn8Ji1jwUYa6aP7Vh1eUGZ97Xsd/v6G0itWOGLMjCY2gI54Da6', 'hanaro', 'ROLE_ADMIN', NOW(), NOW());


-- Product
INSERT INTO Product (
    id, name, paymentCycle, period, interestRate, cancelRate,
    productType, paymentAmount, createdAt, updatedAt
)
VALUES
    (1, '정기적금 상품', 'WEEKLY', 5, 2.0, 1.2, 'SAVING', 10000, NOW(), NOW()),
    (2, '정기예금 상품', 'MONTHLY', 24, 1.0, 0.5, 'DEPOSIT', 0, NOW(), NOW());


-- Product Image
INSERT INTO ProductImage (
    id, product_id, orgName, saveName, saveDir,
    isThumbnail, remark, createdAt, updatedAt
)
VALUES
    (1, 1, 'saving.png', 'saving-uuid.png', 'upload/20260319', true, '적금 썸네일', NOW(), NOW()),
    (2, 2, 'deposit.png', 'deposit-uuid.png', 'upload/20260319', true, '예금 썸네일', NOW(), NOW());


-- Account
INSERT INTO Account (
    id, accountNumber, member_id, balance, status,
    startDate, createdAt, updatedAt
)
VALUES
    (1, '123-4566-7890', 1, 100000, 'ACTIVE', CURDATE(), NOW(), NOW()),
    (2, '222-3333-4440', 2, 200000, 'ACTIVE', CURDATE(), NOW(), NOW());

INSERT INTO Account (accountNumber, member_id, balance, status, startDate, createdAt, updatedAt)
VALUES ('123-0000-0000', 1, 0, 'ACTIVE', NOW(), NOW(), NOW());


-- Subscription
INSERT INTO Subscription (
    id, member_id, product_id, account_id,
    status, startDate, canceled,
    paidCount, interestRate, period,
    paymentAmount, createdAt, updatedAt
)
VALUES
    (1, 1, 1, 1, 'ACTIVE', CURDATE(), false, 0, 2.0, 12, 10000, NOW(), NOW()),
    (2, 2, 2, 2, 'ACTIVE', CURDATE(), false, 0, 1.0, 24, 0, NOW(), NOW());
