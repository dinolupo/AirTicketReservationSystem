--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.8
-- Dumped by pg_dump version 9.5.1

-- Started on 2016-07-31 20:05:56 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE database;
--
-- TOC entry 2014 (class 1262 OID 16385)
-- Name: database; Type: DATABASE; Schema: -; Owner: database
--

CREATE DATABASE database WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8';


ALTER DATABASE database OWNER TO database;

\connect database

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2015 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 1 (class 3079 OID 11861)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2017 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 174 (class 1259 OID 16670)
-- Name: airplane; Type: TABLE; Schema: public; Owner: database
--

CREATE TABLE airplane (
    id bigint NOT NULL,
    modelnumber character varying(255) NOT NULL
);


ALTER TABLE airplane OWNER TO database;

--
-- TOC entry 176 (class 1259 OID 16677)
-- Name: flight; Type: TABLE; Schema: public; Owner: database
--

CREATE TABLE flight (
    id bigint NOT NULL,
    arrivaldate timestamp with time zone NOT NULL,
    departuredate timestamp with time zone NOT NULL,
    flightnumber character varying(8) NOT NULL,
    flightstatus integer NOT NULL,
    fromairport character varying(255) NOT NULL,
    toairport character varying(255) NOT NULL,
    version bigint NOT NULL
	
);


ALTER TABLE flight OWNER TO database;

--
-- TOC entry 175 (class 1259 OID 16675)
-- Name: flight_id_seq; Type: SEQUENCE; Schema: public; Owner: database
--

CREATE SEQUENCE flight_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE flight_id_seq OWNER TO database;

--
-- TOC entry 2018 (class 0 OID 0)
-- Dependencies: 175
-- Name: flight_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: database
--

ALTER SEQUENCE flight_id_seq OWNED BY flight.id;


--
-- TOC entry 173 (class 1259 OID 16668)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: database
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO database;

--
-- TOC entry 1892 (class 2604 OID 16680)
-- Name: id; Type: DEFAULT; Schema: public; Owner: database
--

ALTER TABLE ONLY flight ALTER COLUMN id SET DEFAULT nextval('flight_id_seq'::regclass);


--
-- TOC entry 2007 (class 0 OID 16670)
-- Dependencies: 174
-- Data for Name: airplane; Type: TABLE DATA; Schema: public; Owner: database
--



--
-- TOC entry 2009 (class 0 OID 16677)
-- Dependencies: 176
-- Data for Name: flight; Type: TABLE DATA; Schema: public; Owner: database
--

INSERT INTO flight VALUES (1, '2016-08-15 09:00:00', '2016-07-15 06:00:00', 'DLH18631', 0, 'Berlin', 'Paris', 0);
INSERT INTO flight VALUES (2, '2016-08-16 09:00:00', '2016-07-16 06:00:00', 'DLH18632', 0, 'Berlin', 'Paris', 0);
INSERT INTO flight VALUES (3, '2016-08-25 09:00:00', '2016-07-25 06:00:00', 'DLH18633', 0, 'Berlin', 'Paris', 0);
INSERT INTO flight VALUES (4, '2016-08-26 09:00:00', '2016-07-26 06:00:00', 'DLH18634', 0, 'Berlin', 'Paris', 0);
INSERT INTO flight VALUES (5, '2016-08-27 09:00:00', '2016-07-27 06:00:00', 'DLH18635', 0, 'Berlin', 'Paris', 0);
INSERT INTO flight VALUES (6, '2016-09-28 09:00:00', '2016-07-28 06:00:00', 'DLH18636', 0, 'Berlin', 'Paris', 0);
INSERT INTO flight VALUES (7, '2016-09-29 09:00:00', '2016-07-29 06:00:00', 'DLH18637', 0, 'Berlin', 'Paris', 0);
INSERT INTO flight VALUES (8, '2016-10-26 09:00:00', '2016-07-26 06:00:00', 'DLH18638', 0, 'Berlin', 'Paris', 0);


--
-- TOC entry 2019 (class 0 OID 0)
-- Dependencies: 175
-- Name: flight_id_seq; Type: SEQUENCE SET; Schema: public; Owner: database
--

SELECT pg_catalog.setval('flight_id_seq', 100, true);


--
-- TOC entry 2020 (class 0 OID 0)
-- Dependencies: 173
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: database
--

SELECT pg_catalog.setval('hibernate_sequence', 1, false);


--
-- TOC entry 1894 (class 2606 OID 16674)
-- Name: airplane_pkey; Type: CONSTRAINT; Schema: public; Owner: database
--

ALTER TABLE ONLY airplane
    ADD CONSTRAINT airplane_pkey PRIMARY KEY (id);


--
-- TOC entry 1896 (class 2606 OID 16685)
-- Name: flight_pkey; Type: CONSTRAINT; Schema: public; Owner: database
--

ALTER TABLE ONLY flight
    ADD CONSTRAINT flight_pkey PRIMARY KEY (id);


--
-- TOC entry 2016 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-07-31 20:05:57 CEST

--
-- PostgreSQL database dump complete
--

