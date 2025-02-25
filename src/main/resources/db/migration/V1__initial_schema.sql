create table public.chart (
                              price float(53),
                              coin_id bigint,
                              created_on timestamp(6),
                              id bigint generated by default as identity,
                              primary key (id)
);

create table public.coin (
                             price float(53),
                             created_on timestamp(6),
                             id bigint generated by default as identity,
                             description varchar(255),
                             img_url varchar(255),
                             name varchar(255) unique,
                             primary key (id)
);

create table public.order (
                              amount float(53) not null,
                              price float(53) not null,
                              coin_id bigint,
                              created_on timestamp(6),
                              id bigint generated by default as identity,
                              user_id bigint,
                              order_type varchar(255) not null check (order_type in ('ORDER_BUY','ORDER_SELL')),
                              primary key (id)
);

create table public.user (
                             balance float(53) not null,
                             invested float(53),
                             is_enabled boolean not null,
                             created_on timestamp(6),
                             id bigint generated by default as identity,
                             password varchar(255) not null,
                             role_type varchar(255) not null check (role_type in ('ROLE_USER','ROLE_ADMIN')),
                             username varchar(255) not null,
                             primary key (id)
);

create table public.user_coin (
                                  amount float(53),
                                  coin_id bigint,
                                  created_on timestamp(6),
                                  id bigint generated by default as identity,
                                  user_id bigint,
                                  primary key (id)
);

create table public.news_subscriber (
                             created_on timestamp(6),
                             id bigint generated by default as identity,
                             email varchar(255) not null,
                             primary key (id)
);

alter table if exists public.chart
    add constraint fk_chart_coin
        foreign key (coin_id)
            references public.coin;

alter table if exists public.order
    add constraint fk_order_coin
        foreign key (coin_id)
            references public.coin;

alter table if exists public.order
    add constraint fk_order_user
        foreign key (user_id)
            references public.user;

alter table if exists public.user_coin
    add constraint fk_user_coin_coin
        foreign key (coin_id)
            references public.coin;

alter table if exists public.user_coin
    add constraint fk_user_coin_user
        foreign key (user_id)
            references public.user;
