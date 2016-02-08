/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012 gnosygnu@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package gplx.dbs.qrys; import gplx.*; import gplx.dbs.*;
import gplx.dbs.sqls.*; import gplx.dbs.sqls.itms.*;
public class Db_qry_insert implements Db_arg_owner {
	public Db_qry_insert(String base_table) {this.base_table = base_table;}
	public int			Tid()					{return Db_qry_.Tid_insert;}
	public boolean			Exec_is_rdr()			{return false;}
	public String		To_sql__exec(Sql_qry_wtr wtr) {return wtr.To_sql_str(this, false);}		
	public int			Exec_qry(Db_conn conn)	{return conn.Exec_qry(this);}
	public String		Base_table()			{return base_table;} private String base_table;
	public String[]		Cols_for_insert() {return cols_for_insert;} private String[] cols_for_insert;
	public Db_arg_owner From_(String tbl) {base_table = tbl; return this;}
	public KeyValHash	Args() {return args;} private final KeyValHash args = KeyValHash.new_();
	public Db_arg_owner Val_byte(String k, byte v)				{return Val_obj_type(k, v, Db_val_type.Tid_byte);}
	public Db_arg_owner Val_int(String k, int v)				{return Val_obj_type(k, v, Db_val_type.Tid_int32);}
	public Db_arg_owner Val_long(String k, long v)				{return Val_obj_type(k, v, Db_val_type.Tid_int64);}
	public Db_arg_owner Val_decimal(String k, Decimal_adp v)	{return Val_obj_type(k, v.Under(), Db_val_type.Tid_decimal);}
	public Db_arg_owner Val_str(String k, String v)				{return Val_obj_type(k, v, Db_val_type.Tid_varchar);}
	public Db_arg_owner Val_str_by_bry(String k, byte[] v)		{return Val_obj_type(k, String_.new_u8(v), Db_val_type.Tid_varchar);}
	public Db_arg_owner Val_date(String k, DateAdp v)			{return Val_obj_type(k, v, Db_val_type.Tid_date);}
	public Db_arg_owner Val_blob(String k, byte[] v)			{return Val_obj_type(k, v, Db_val_type.Tid_bry);}
	public Db_arg_owner Val_obj(String k, Object v)				{return Val_obj_type(k, v, Db_val_type.Tid_null);}
	public Db_arg_owner Val_obj_type(String key, Object val, byte val_tid) {
		if (key == Dbmeta_fld_itm.Key_null) return this;
		args.Add(key, new Db_arg(key, val, val_tid));
		return this;
	}
	public Db_arg_owner Crt_int(String k, int v)		{return Val_obj_type(k, v, Db_val_type.Tid_int32);}
	public Db_arg_owner Crt_str(String k, String v)		{return Val_obj_type(k, v, Db_val_type.Tid_varchar);}
	public Db_qry__select_cmd Select() {return select;} Db_qry__select_cmd select;
	public Db_qry_insert Select_(Db_qry__select_cmd qry) {this.select = qry; return this;}
	public Db_qry_insert Cols_(String... ary) {
		if (cols == null) cols = new Sql_select_fld_list();
		for (String fld : ary)
			cols.Add(Sql_select_fld.New_fld(Sql_select_fld.Tbl_null, fld, fld));
		return this;
	}
	public Sql_select_fld_list Cols() {return cols;} private Sql_select_fld_list cols;

	public static Db_qry_insert new_() {return new Db_qry_insert();} Db_qry_insert() {}
	public static Db_qry_insert new_(String tbl, String... keys) {
		Db_qry_insert rv = Db_qry_insert.new_();
		rv.base_table = tbl;
		rv.cols_for_insert = keys;
		int len = keys.length;
		for (int i = 0; i < len; ++i)
			rv.Val_obj(keys[i], null);
		return rv;
	}
}
