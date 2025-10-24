CREATE TABLE orders
(
    id varchar(36) NOT NULL,
    id_user varchar(36) NOT NULL,
    date timestamptz NOT NULL,
    total decimal(12,2) NOT NULL,
    items_json text,
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);
