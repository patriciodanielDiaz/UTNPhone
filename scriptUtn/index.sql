-------------------------------------------------indices-----------------------------------------------------------------------

--INDEX RANGE SCAN

--tabla creada (date_index) para aumentar la performance al crear un indice BTREE
EXPLAIN EXTENDED SELECT * FROM calls c WHERE c.origincall=2 and c.create_at between "2020-01-01" and "2020-07-12";
CREATE INDEX idx_call_date ON calls(date_index) USING BTREE;

--tabla creada (date_index) para aumentar la performance al crear un indice BTREE
--INDEX RANGE SCAN
EXPLAIN EXTENDED SELECT i.* FROM invoices i inner join lines_users li on li.idline=i.idline where li.linenumber="+54 (9) 223 154211100" and i.create_at between "2020-01-01" and "2020-07-12" ORDER BY i.state ASC;
CREATE INDEX idx_invoice_date ON invoices(date_index) USING BTREE;

--show INDEX from calls

--Crear indice BTREE para "between" para fechas
--Craer indice  HASHTABLE para buscar los numeros de telefonos o nombres cuando haces "="
