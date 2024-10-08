-- Create Customer Table
CREATE TABLE IF NOT EXISTS customer (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL UNIQUE,
    contact VARCHAR(255),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create Project Table
CREATE TABLE IF NOT EXISTS project (
                                       id SERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    customer_id INTEGER,
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
    );

-- Insert Sample Customers
INSERT INTO customer (name, contact, creation_date) VALUES
('John Doe', 'john.doe@example.com', '2024-10-01T10:00:00'),
('Jane Smith', 'jane.smith@example.com', '2024-10-02T11:00:00');

-- Insert Sample Projects and link to Customers
INSERT INTO project (name, description, creation_date, customer_id) VALUES
('Project Alpha', 'A test project for John Doe', '2024-10-03T12:00:00', 1),
('Project Beta', 'Another test project for John Doe', '2024-10-03T13:00:00', 1),
('Project Gamma', 'Test project for Jane Smith', '2024-10-03T14:00:00', 2);
