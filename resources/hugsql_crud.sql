-- Users CRUD:
------------------------------------------------

-- :name get-user :? :1
select * from users
where id = :id

-- :name list-users :? :*
select * from users

-- :name add-user :<! :n
insert into users (first_name, last_name, email, phone, gender, create_date, modify_date)
values (:first_name, :last_name, :email, :phone, :gender, current_timestamp, current_timestamp) returning id

-- :name update-user :! :n
update users set first_name = :first_name, last_name = :last_name, email = :email, phone = :phone, gender = :gender, modify_date = current_timestamp
where id = :id

-- :name delete-user :! :n
delete from users
where id = :id