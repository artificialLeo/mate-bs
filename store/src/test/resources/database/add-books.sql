INSERT INTO books (id, title, author, isbn, price, description, cover_image, category_ids)
VALUES
    (1, 'Book Title 1', 'Author 1', 'ISBN-123456', 19.99, 'Description 1', 'cover1.jpg', ARRAY[1, 2]),
    (2, 'Book Title 2', 'Author 2', 'ISBN-654321', 29.99, 'Description 2', 'cover2.jpg', ARRAY[3, 4, 5]);
