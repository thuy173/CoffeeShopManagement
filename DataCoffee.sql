create database coffeeshop;
use coffeeshop;


SELECT date, COUNT(DISTINCT customer_id) FROM customer WHERE 
date >= 1 GROUP BY date ORDER BY TIMESTAMP(date);
  
  

select * from customer;
select * from order_item;
select * from product;
select * from orders;
select * from receipt;
SELECT SUM(price) FROM customer WHERE customer_id =1 ;

show create table product; 

 CREATE TABLE role(
 role_id INT AUTO_INCREMENT PRIMARY KEY,
 name varchar(30)
 );
 Create table admin(
admin_id INT auto_increment primary key,
username varchar(32),
password varchar(32),
role_id int,
FOREIGN KEY (role_id) REFERENCES role(role_id)
);

insert into admin(username, password, role_id) values
("Customer", "12345678",2),
("Ta Thi Thanh Thuy", "12345678",1);

insert into role(name) values
("admin"),
("customer");
GRANT ALL PRIVILEGES ON coffeeshop.* TO 'admin'@'localhost';
GRANT SELECT ON coffeeshop.customers TO 'customer'@'localhost';
REVOKE ALL PRIVILEGES ON coffeeshop.* FROM 'customer'@'localhost';

SELECT role.name FROM admin INNER JOIN role ON admin.role_id = role.role_id WHERE admin.username = "Customer";


 CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(15),
    loyalty_points INT -- Điểm thưởng
);

CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id int,
    prod_name VARCHAR(50),
    type VARCHAR(50),
    quantity int,
    price DECIMAL(10, 2),
    date date,
    image text,
    em_username varchar(50)
);

-- Create the Products table
CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100),
    description TEXT, -- Mô tả
    category VARCHAR(50), -- Danh mục, ví dụ: cà phê, trà, bánh ngọt
    image text,
    price DECIMAL(10, 2),
    quantity int, -- Số lượng
    ingredients TEXT, -- Thành phần
    availability BOOLEAN -- Tình trạng có sẵn hay không
);



-- Create the Orders table
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    order_date DATE, -- Ngày đặt hàng
    total_amount DECIMAL(10, 2), -- Tổng số tiền
    payment_method VARCHAR(50), -- Phương thức thanh toán
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Create the OrderItems table
CREATE TABLE order_item (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT,
    item_price DECIMAL(10, 2), -- Giá mặt hàng
    subtotal DECIMAL(10, 2), -- Tổng cộng cho mỗi mặt hàng
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);
ALTER TABLE order_item
ADD discount DECIMAL(10, 2);


-- Create the Inventory table
CREATE TABLE inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    name_ingredient varchar(100), -- tên nguyên liệu
    quantity_in_stock INT, -- Số lượng tồn kho
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- Create the Employees table
CREATE TABLE employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    job_title VARCHAR(50),
    hire_date DATE, -- Ngày tuyển dụng
    month_now int, -- Tháng hiện tại
    number_of_working_hours float, -- số giờ làm việc trong tháng
    salary DECIMAL(10, 2)
);

-- Create the Schedules table - Lịch Làm Việc Của Nhân Viên
CREATE TABLE schedule (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    start_time TIME, -- Thời gian bắt đầu
    end_time TIME,
    day_of_week VARCHAR(20),
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
);

-- Create the Expenses table
CREATE TABLE expense (
    expense_id INT AUTO_INCREMENT PRIMARY KEY,
    expense_type VARCHAR(100), -- Loại chi phí
    expense_date DATE, -- Ngày chi phí
    amount DECIMAL(10, 2), -- Số tiền
    description TEXT -- Mô tả
);

-- Create the Feedback table
CREATE TABLE feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    feedback_date DATE,
    feedback_text TEXT, -- Nội dung phản hồi
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Create the Transactions table
CREATE TABLE transaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_date DATE, -- Ngày ghi chép
    description TEXT, -- Mô tả
    income DECIMAL(10, 2), -- Thu nhập
    expenses DECIMAL(10, 2), -- Chi phí
    profit DECIMAL(10, 2) -- Lợi nhuận
);

CREATE TABLE receipt (
    receipt_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    order_id INT,
    receipt_date DATE, -- ngày in hóa đơn
    total_amount DECIMAL(10, 2),
    payment_method VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

INSERT INTO product (product_name, description, category, image, price, quantity, ingredients, availability)
VALUES
    ('Espresso', 'A strong and rich espresso coffee', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee.jpeg', 2.99, 200, 'Coffee beans, water', true),
    ('Green Tea', 'Premium green tea leaves', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea1.jpeg', 4.49, 100, 'Green tea leaves, hot water', true),
    ('Chocolate Cake', 'Delicious chocolate cake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake1.jpeg', 19.99, 50, 'Flour, sugar, cocoa, eggs', true),
    ('Latte', 'Smooth and creamy latte coffee', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee1.jpeg', 3.49, 150, 'Coffee beans, milk', true),
    ('Herbal Tea', 'A blend of soothing herbal teas', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea2.jpeg', 5.99, 75, 'Herbal tea blend, hot water', true),
    ('Fruit Cake', 'Fruity and moist fruit cake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake2.jpeg', 14.99, 30, 'Flour, sugar, fruits, eggs', true),
    ('Cappuccino', 'Classic cappuccino with foam', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee2.jpeg', 4.99, 120, 'Coffee beans, milk, foam', true),
    ('Chai Tea', 'Spiced chai tea with a hint of sweetness', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea3.jpeg', 6.49, 80, 'Chai tea blend, milk', true),
    ('Cheesecake', 'Creamy and decadent cheesecake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake3.jpeg', 22.99, 25, 'Cream cheese, graham cracker crust', true),
    ('Iced Coffee', 'Refreshing iced coffee', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee3.jpeg', 3.99, 180, 'Coffee concentrate, ice, milk', true);
    
INSERT INTO product (product_name, description, category, image, price, quantity, ingredients, availability)
VALUES
    ('Orange Juice', 'Freshly squeezed orange juice', 'Juice', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\juice7.jpeg', 3.99, 100, 'Oranges', true),
    ('Apple Juice', '100% pure apple juice', 'Juice', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\juice2.jpeg', 2.99, 80, 'Apples', true),
    ('Grape Juice', 'Sweet and tangy grape juice', 'Juice', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\juice5.jpeg', 4.49, 70, 'Grapes', true),
    ('Mixed Berry Smoothie', 'A refreshing blend of mixed berries', 'Juice', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\juice9.jpeg', 5.99, 50, 'Mixed berries, yogurt', true),
    ('Pineapple Juice', 'Tropical pineapple juice', 'Juice', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\juice3.jpeg', 3.49, 90, 'Pineapples', true),
    ('Mango Tango Smoothie', 'A tropical delight with mango and passion fruit', 'Juice', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\juice4.jpeg', 6.49, 60, 'Mangoes, passion fruit, yogurt', true),
    ('Cranberry Juice', 'Tart and refreshing cranberry juice', 'Juice', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\juice8.jpeg', 4.29, 75, 'Cranberries', true),
    ('Watermelon Juice', 'Cool and hydrating watermelon juice', 'Juice', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\juice10.jpeg', 3.99, 85, 'Watermelons', true),
    ('Carrot Orange Juice', 'A healthy blend of carrots and oranges', 'Juice', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\juice9.jpeg', 4.79, 65, 'Carrots, oranges', true),
    ('Lemonade', 'Classic homemade lemonade', 'Juice', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\juice1.jpeg', 2.49, 120, 'Lemons, sugar, water', true);
    
INSERT INTO product (product_name, description, category, image, price, quantity, ingredients, availability)
VALUES
    ('Caramel Latte', 'A sweet and creamy caramel latte', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee.jpeg', 4.99, 90, 'Coffee beans, caramel syrup, milk', true),
    ('Vanilla Cappuccino', 'Smooth cappuccino with a hint of vanilla', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee1.jpeg', 4.79, 80, 'Coffee beans, vanilla extract, milk', true),
    ('Iced Mocha', 'Chilled mocha coffee with chocolate', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee13.jpeg', 4.49, 70, 'Coffee beans, cocoa, milk, ice', true),
    ('Espresso Macchiato', 'Espresso with a dollop of foamed milk', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee3.jpeg', 3.99, 60, 'Coffee beans, milk foam', true),
    ('Hazelnut Coffee', 'Coffee with a delightful hazelnut flavor', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee4.jpeg', 5.49, 75, 'Coffee beans, hazelnut syrup, milk', true),
    ('Decaf Espresso', 'A decaffeinated version of classic espresso', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee11.jpeg', 3.49, 85, 'Decaffeinated coffee beans, water', true),
    ('Irish Coffee', 'Classic Irish coffee with a kick', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee6.jpeg', 6.99, 50, 'Coffee, Irish whiskey, sugar, cream', true),
    ('Mint Mocha', 'Refreshing mocha coffee with a hint of mint', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee7.jpeg', 5.99, 65, 'Coffee beans, cocoa, mint extract, milk', true),
    ('Pumpkin Spice Latte', 'Seasonal favorite with pumpkin spice', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee8.jpeg', 4.29, 55, 'Coffee beans, pumpkin spice syrup, milk', true),
    ('Cold Brew', 'Smooth and strong cold brew coffee', 'Coffee', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\coffee9.jpeg', 4.79, 100, 'Cold brew coffee concentrate', true);
    
    INSERT INTO product (product_name, description, category, image, price, quantity, ingredients, availability)
VALUES
    ('Red Velvet Cake', 'Velvety and indulgent red velvet cake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake2.jpeg', 19.99, 40, 'Flour, sugar, cocoa, buttermilk', true),
    ('Chocolate Fudge Cake', 'Rich and decadent chocolate fudge cake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake3.jpeg', 22.99, 35, 'Flour, sugar, cocoa, eggs', true),
    ('Carrot Cake', 'Moist carrot cake with cream cheese frosting', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake1.jpeg', 16.99, 30, 'Flour, sugar, carrots, cream cheese', true),
    ('Lemon Pound Cake', 'Zesty lemon-flavored pound cake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake5.jpeg', 18.49, 25, 'Flour, sugar, lemon zest, butter', true),
    ('Strawberry Shortcake', 'Delicate strawberry shortcake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake7.jpeg', 14.99, 40, 'Flour, sugar, strawberries, whipped cream', true),
    ('Classic Cheesecake', 'Creamy and classic cheesecake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake8.jpeg', 20.99, 20, 'Cream cheese, graham cracker crust', true),
    ('Coconut Cake', 'Coconut-infused tropical cake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake10.jpeg', 17.99, 30, 'Flour, sugar, coconut flakes', true),
    ('Marble Cake', 'Swirled chocolate and vanilla marble cake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake12.jpeg', 15.99, 35, 'Flour, sugar, cocoa, vanilla', true),
    ('Blueberry Crumble Cake', 'Sweet blueberry crumble cake', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake15.jpeg', 16.99, 25, 'Flour, sugar, blueberries, crumble topping', true),
    ('Raspberry Almond Cake', 'Almond-flavored cake with raspberries', 'Cake', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake6.jpeg', 19.49, 30, 'Flour, sugar, almond extract, raspberries', true);
    
    INSERT INTO product (product_name, description, category, image, price, quantity, ingredients, availability)
VALUES
    ('Earl Grey Tea', 'Classic black tea with bergamot flavor', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea4.jpeg', 3.99, 60, 'Black tea leaves, bergamot essence', true),
    ('Green Tea', 'Premium green tea leaves', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea5.jpeg', 4.49, 80, 'Green tea leaves, hot water', true),
    ('Chai Tea', 'Spiced chai tea with a hint of sweetness', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea1.jpeg', 4.99, 70, 'Chai tea blend, milk', true),
    ('Jasmine Tea', 'Fragrant and soothing jasmine tea', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea2.jpeg', 3.79, 90, 'Jasmine tea leaves, hot water', true),
    ('Herbal Tea Sampler', 'Assorted herbal tea flavors', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea3.jpeg', 5.99, 50, 'Various herbal tea blends', true),
    ('Peppermint Tea', 'Refreshing and invigorating peppermint tea', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea6.jpeg', 4.29, 75, 'Peppermint leaves, hot water', true),
    ('Lemon Ginger Tea', 'Zesty lemon and ginger infusion', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea7.jpeg', 4.79, 60, 'Lemon, ginger, hot water', true),
    ('Blackberry Tea', 'Sweet and fruity blackberry tea', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea8.jpeg', 5.49, 65, 'Blackberry tea blend, hot water', true),
    ('Hibiscus Tea', 'Tangy and vibrant hibiscus tea', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\tea4.jpeg', 4.99, 70, 'Hibiscus petals, hot water', true),
    ('Mint Tea', 'Cool and soothing mint tea', 'Tea', 'C:\\Users\\HP\\IdeaProjects\\coffeeShopManagement\\src\\main\\java\\com\\example\\coffeeshopmanagement\\image\\cake17.jpeg', 4.49, 85, 'Mint leaves, hot water', true);
    
    -- Inserting a second employee
INSERT INTO employee (first_name, last_name, job_title, hire_date, salary)
VALUES ('Jane', 'Smith', 'Barista', '2023-09-27', 9.50);

-- Inserting a third employee
INSERT INTO employee (first_name, last_name, job_title, hire_date, salary)
VALUES ('Michael', 'Johnson', 'Manager', '2023-09-28', 15.00);

-- Inserting a fourth employee
INSERT INTO employee (first_name, last_name, job_title, hire_date, salary)
VALUES ('Emily', 'Davis', 'Cashier', '2023-09-29', 8.75);
INSERT INTO employee (first_name, last_name, job_title, hire_date, salary)
VALUES ('William', 'Brown', 'Barista', '2023-09-30', 9.50);
INSERT INTO employee (first_name, last_name, job_title, hire_date, salary)
VALUES ('Sophia', 'Miller', 'Barista', '2023-10-03', 9.50);
INSERT INTO employee (first_name, last_name, job_title, hire_date, salary)
VALUES ('James', 'Lee', 'Serve', '2023-10-02', 9.50);
    
    
    
    
    use coffeeshop;
    select * from product;
    
    SHOW TRIGGERS;


    
