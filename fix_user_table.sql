-- Add missing columns to User table for login lockout feature
ALTER TABLE User 
ADD COLUMN failed_attempts INT DEFAULT 0,
ADD COLUMN locked_until TIMESTAMP NULL;

-- Verify the changes
DESCRIBE User;
