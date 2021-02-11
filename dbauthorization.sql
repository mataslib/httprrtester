-- Opravneni

-- Vytvoreni skupiny pro uzivatele aplikace
CREATE
GROUP app_users;
-- Prirazeni vsech opravneni na tabulce testy skupine uzivatelu aplikace
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE test TO GROUP app_users;
-- Prirazeni opravneni na generatoru id skupine uzivatelu aplikace
GRANT USAGE, SELECT ON SEQUENCE test_id_seq TO app_users;

-- Zapnuti funkce ROW LEVEL SECURITY - opravneni (policy) pro jednotlive radky
ALTER TABLE test ENABLE ROW LEVEL SECURITY;

-- Vsichni mohou insertovat
CREATE
POLICY insert_all ON test
    FOR
INSERT WITH CHECK (true);

-- Author testu muze na svem zaznamu delat vse
CREATE
POLICY author_all ON test
    USING (test.dbuser = CURRENT_USER);

-- Ostatni mohou zaznam cist pokud je sdilen ke cteni
CREATE
POLICY other_read_if_shared_for_read ON test
    FOR
SELECT
    USING
    (test.shared_for_read = true);

-- Ostatni mohou zaznam upravit a cist (update pouziva select) pokud je sdilen k uprave
CREATE
POLICY other_update_if_shared_for_update ON test
    USING (test.shared_for_update = true);
