CREATE TABLE order_item
(
    id varchar(36) NOT NULL,
    order_id varchar(36) NOT NULL,
    product_id varchar(36) NOT NULL,
    quantity integer NOT NULL,
    price decimal(10,2) NOT NULL,
    CONSTRAINT order_item_pkey PRIMARY KEY (id),
    CONSTRAINT fk_order FOREIGN KEY (order_id)
        REFERENCES orders (id)
        ON DELETE CASCADE
);
