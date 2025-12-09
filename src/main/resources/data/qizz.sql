CREATE DATABASE IF NOT EXISTS quizz_db;
USE quizz_db;

CREATE TABLE users(
	user_id INT PRIMARY KEY AUTO_INCREMENT,
	username varchar(50) not null unique,
	password varchar(50) not null,
    email varchar(100) not null unique,
    full_name varchar(100) not null,
    role enum ('STUDENT', 'ADMIN', 'TEACHER') not null default 'STUDENT',
    created_at timestamp default current_timestamp   
);

create table classes(
	class_id int primary key auto_increment,
    class_name varchar(100) not null,
    teacher_id int not null,
    access_code varchar(10) not null,
    created_at timestamp default current_timestamp,
    foreign key (teacher_id) references users(user_id)
);

create table class_enrollments(
	class_id int not null,
    student_id int not null,
    joined_at timestamp default current_timestamp,
    primary key (class_id, student_id),
    foreign key (class_id) references classes(class_id) on delete cascade,
    foreign key (student_id) references users(user_id) on delete cascade
);

create table quizzes(
	quiz_id int primary key auto_increment,
    creator_id int not null,
    title varchar(100) not null,
    description text,
    time_limit int,
    status enum ('draft', 'published', 'archived') default 'draft',
	created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    foreign key (creator_id) references users(user_id)
);

create table question(
	question_id int primary key auto_increment,
    question_text text not null,
    question_type enum('multiple_choice','true_false','short_answer') default 'multiple_choice',
    explanation text,
    creator_id int not null,
    created_at timestamp default current_timestamp,
    foreign key (creator_id) references users(user_id)
);

create table answer(
	answer_id int primary key auto_increment,
    question_id int not null,
    answer_text text,
    is_correct boolean default false,
    foreign key (question_id) references question(question_id) on delete cascade
);

CREATE TABLE quiz_questions (
    quiz_id INT NOT NULL,
    question_id INT NOT NULL,
    question_order INT, -- Thứ tự câu hỏi trong bài quiz (nếu cần)
    PRIMARY KEY (quiz_id, question_id),
    FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES question(question_id) ON DELETE CASCADE
);

create table quiz_assignments(
	assignment_id int primary key auto_increment,
    quiz_id int not null,
    class_id int not null,
    assigned_by_id int not null,
    due_date timestamp,
    created_at timestamp default current_timestamp,
    foreign key (quiz_id) references quizzes(quiz_id),
    foreign key (class_id) references classes(class_id),
    foreign key (assigned_by_id) references users(user_id)
);

CREATE TABLE results (
    result_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL, 
    quiz_id INT NOT NULL,
    score DECIMAL(5, 2), 
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id)
);

CREATE TABLE user_answers (
    user_answer_id INT PRIMARY KEY AUTO_INCREMENT,
    result_id INT NOT NULL, -- Thuộc lần làm bài nào (bảng Results)
    question_id INT NOT NULL,
    selected_answer_id INT, -- Phương án (bảng Answers) mà student đã chọn
    is_correct BOOLEAN, -- Lưu lại đúng/sai để truy vấn nhanh
    FOREIGN KEY (result_id) REFERENCES results(result_id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES question(question_id),
    FOREIGN KEY (selected_answer_id) REFERENCES answer(answer_id)
);

-- --------------------------------------------------
-- Helpful example and normalization statements
-- --------------------------------------------------
-- If your existing data uses lowercase role values (e.g. 'admin'),
-- you can normalize to uppercase with the following (test on staging first):
-- UPDATE users SET role = UPPER(role);

-- Example seed inserts (commented out). Uncomment and adjust before running.
-- INSERT INTO users (username, password, email, full_name, role) VALUES
--   ('admin', 'CHANGE_ME_PASSWORD', 'admin@example.com', 'Site Admin', 'ADMIN'),
--   ('teacher1', 'CHANGE_ME_PASSWORD', 'teacher1@example.com', 'Teacher One', 'TEACHER'),
--   ('student1', 'CHANGE_ME_PASSWORD', 'student1@example.com', 'Student One', 'STUDENT');


