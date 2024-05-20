create table customer (customer_id bigserial not null, name varchar(30) not null, address varchar(255), contact_info varchar(255), primary key (customer_id));
create table inventory_transactions (quantity integer not null, location_id bigint, product_id bigint, transaction_date timestamp(6) with time zone not null, transaction_id bigserial not null, notes varchar(255), transaction_type varchar(255), primary key (transaction_id));
create table payment_status (volume_fee float(53), payment_id bigserial not null, purchase_order_id bigint unique, receiver_city varchar(255), send_city varchar(255) not null, tax_id varchar(255), primary key (payment_id));
create table product (price float(53) not null, quantity_available integer, reorder_point integer, product_id bigserial not null, supplier_id bigint, description varchar(255), name varchar(255) not null, primary key (product_id));
create table purchase_order (total_price float(53), customer_id bigint, delivery_date timestamp(6) with time zone, order_date timestamp(6) with time zone not null, purchase_order_id bigserial not null, payment_method varchar(255), status varchar(255), primary key (purchase_order_id));
create table purchase_order_item (price float(53), quantity integer, order_details_id bigserial not null, product_id bigint, purchase_order_id bigint, primary key (order_details_id));
create table shipment (arrival_date timestamp(6) with time zone, purchase_order_id bigint unique, shipment_date timestamp(6) with time zone not null, shipment_id bigserial not null, supplier_id bigint, status varchar(255), primary key (shipment_id));
create table shipment_detail (quantity integer not null, unit_price float(53) not null, product_id bigint unique, shipment_details_id bigserial not null, shipment_id bigint, primary key (shipment_details_id));
create table storage_location (capacity integer, occupied_space integer, location_id bigserial not null, warehouse_id bigint, name varchar(255) not null, primary key (location_id));
create table supplier (supplier_id bigserial not null, contact_info varchar(255), name varchar(255) not null, primary key (supplier_id));
create table warehouse_location (warehouse_id bigserial not null, location varchar(255) not null, name varchar(255) not null, warehouse_manager varchar(255), primary key (warehouse_id));
alter table if exists inventory_transactions add constraint FKni83thfc7v0jx8f041hwpphp3 foreign key (location_id) references storage_location;
alter table if exists inventory_transactions add constraint FKdb8xs8ownfacr7i0i7ij91bem foreign key (product_id) references product;
alter table if exists payment_status add constraint FKd05ehydbpy6ph73igugfebf55 foreign key (purchase_order_id) references purchase_order;
alter table if exists product add constraint FK2kxvbr72tmtscjvyp9yqb12by foreign key (supplier_id) references supplier;
alter table if exists purchase_order add constraint FK158vbkwgyf5r6ogk9nkugqv2c foreign key (customer_id) references customer;
alter table if exists purchase_order_item add constraint FK593lt017d995ds7nuqxgo3mmm foreign key (product_id) references product;
alter table if exists purchase_order_item add constraint FKmj122necubadvuquvjoq967y7 foreign key (purchase_order_id) references purchase_order;
alter table if exists shipment add constraint FKbk23d29y65f0ana2bi4ja7yak foreign key (purchase_order_id) references purchase_order;
alter table if exists shipment add constraint FK9g2a3xvkou96gf3wfwj5flhka foreign key (supplier_id) references supplier;
alter table if exists shipment_detail add constraint FKsg2h15h0n0rcabkycvmkntgkf foreign key (product_id) references product;
alter table if exists shipment_detail add constraint FKkqt5epu0hn4c20bxiw8go6yre foreign key (shipment_id) references shipment;
alter table if exists storage_location add constraint FK2d0fg15tgkegmy168jyfu723m foreign key (warehouse_id) references warehouse_location;