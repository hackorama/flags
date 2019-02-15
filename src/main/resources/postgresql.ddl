#
# Postgresql Specific SQL DML overrides
#
replace : INSERT INTO _STORE_ VALUES (?, ?) ON CONFLICT (k) DO UPDATE SET v = ?
