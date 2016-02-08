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
package gplx.dbs; import gplx.*;
import gplx.dbs.engines.nulls.*; import gplx.dbs.engines.mems.*; import gplx.dbs.engines.sqlite.*; import gplx.dbs.engines.tdbs.*;
import gplx.dbs.engines.mysql.*; import gplx.dbs.engines.postgres.*;
public class Db_conn_info_ {
	public static final Db_conn_info Null			= Noop_conn_info.Instance;
	public static final Db_conn_info Test			= Mysql_conn_info.new_("127.0.0.1", "unit_tests", "root", "mysql7760");
	public static Db_conn_info parse(String raw)		{return Db_conn_info_pool.Instance.Parse(raw);}
	public static Db_conn_info sqlite_(Io_url url)		{return Sqlite_conn_info.load_(url);}
	public static Db_conn_info tdb_(Io_url url)			{return Tdb_conn_info.new_(url);}
	public static Db_conn_info mem_(String db)			{return Db_conn_info__mem.new_(db);}
	public static final String Key_tdb = Tdb_conn_info.Tid_const;
}
class Db_conn_info_pool {
	private Ordered_hash regy = Ordered_hash_.New();
	public Db_conn_info_pool() {
		this.Add(Noop_conn_info.Instance).Add(Tdb_conn_info.Instance).Add(Mysql_conn_info.Instance).Add(Postgres_conn_info.Instance).Add(Sqlite_conn_info.Instance);
		this.Add(Db_conn_info__mem.Instance);
	}
	public Db_conn_info_pool Add(Db_conn_info itm) {regy.Add_if_dupe_use_nth(itm.Tid(), itm); return this;}
	public Db_conn_info Parse(String raw) {// assume each pair has format of: name=val;
		try {
			GfoMsg m = GfoMsg_.new_parse_("db_url");
			String[] terms = String_.Split(raw, ";");
			String url_tid = "";
			for (String term : terms) {
				if (String_.Len(term) == 0) continue;
				String[] kv = String_.Split(term, "=");
				if (String_.Eq(kv[0], "gplx_key"))
					url_tid = kv[1]; // NOTE: do not add to GfoMsg; will not be part of ApiStr
				else
					m.Add(kv[0], kv[1]);
			}
			Db_conn_info prototype = (Db_conn_info)regy.Get_by(url_tid);
			return prototype.New_self(raw, m);
		}
		catch(Exception exc) {throw Err_.new_parse_exc(exc, Db_conn_info.class, raw);}
	}
	public Db_conn_info Parse_or_sqlite_or_fail(String raw) {// assume each pair has format of: name=val;
		GfoMsg msg = GfoMsg_.new_parse_("db_url");
		String[] kvps = String_.Split(raw, ";");
		String cs_tid = null;
		int kvps_len = kvps.length; 
		for (int i = 0; i < kvps_len; ++i) {
			String kvp_str = kvps[i];
			if (String_.Len(kvp_str) == 0) continue;	// ignore empty; EX: "data source=/db.sqlite;;"
			String[] kvp = String_.Split(kvp_str, "=");
			String key = kvp[0], val = kvp[1];
			if (String_.Eq(key, "gplx_key"))
				cs_tid = val;	// NOTE: do not add to GfoMsg; will not be part of ApiStr
			else
				msg.Add(key, val);
		}
		if (cs_tid == null) {	// gplx_key not found; try url as sqlite; EX: "/db.sqlite"
			Io_url sqlite_url = null;
			try {sqlite_url = Io_url_.new_any_(raw);}
			catch (Exception exc) {throw Err_.new_exc(exc, "dbs", "invalid connection String", "raw", raw);}
			msg.Clear();
			cs_tid = Sqlite_conn_info.Tid_const;
			msg.Add(Sqlite_conn_info.Cs__data_source, sqlite_url.Raw());
			msg.Add(Sqlite_conn_info.Cs__version	, Sqlite_conn_info.Cs__version__3);
		}
		Db_conn_info prototype = (Db_conn_info)regy.Get_by(cs_tid);
		return prototype.New_self(raw, msg);
	}
	public static final Db_conn_info_pool Instance = new Db_conn_info_pool();
}
