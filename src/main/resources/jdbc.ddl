#
# Standard SQL DDL 
#
createMultiTable : CREATE TABLE IF NOT EXISTS _TABLE_ (k VARCHAR(_KEY_SIZE_), v VARCHAR(_VALUE_SIZE_))
createTable      : CREATE TABLE IF NOT EXISTS _TABLE_ (k VARCHAR(_KEY_SIZE_), v VARCHAR(_VALUE_SIZE_), PRIMARY KEY (k))
#
# Standard SQL DML 
#
contains         : SELECT k FROM _STORE_ WHERE k = ?
get              : SELECT v FROM _STORE_ WHERE k = ?
getByvalue       : SELECT k FROM _STORE_ WHERE v = ?
getColumnValues  : SELECT _COLUMN_ FROM _STORE_ 
getMultiKey      : SELECT v FROM _STORE_ WHERE k = ?
replace          : REPLACE INTO _STORE_ VALUES (?, ?)
insert           : INSERT INTO _STORE_ VALUES (?, ?)
removeKey        : DELETE FROM _STORE_ WHERE k = ?
removeKeyValue   : DELETE FROM _STORE_ WHERE k = ? AND v = ?