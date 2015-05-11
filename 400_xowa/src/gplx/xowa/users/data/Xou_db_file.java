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
package gplx.xowa.users.data; import gplx.*; import gplx.xowa.*; import gplx.xowa.users.*;
import gplx.dbs.*; import gplx.dbs.cfgs.*; import gplx.xowa.files.caches.*;
class Xou_db_file {
	public Xou_db_file(Db_conn conn) {
		this.tbl__cfg		= new Db_cfg_tbl(conn, "xowa_cfg");
		this.tbl__site		= new Xoud_site_tbl(conn);
		this.tbl__history	= new Xoud_history_tbl(conn);
		this.tbl__cache		= new Xof_cache_tbl(conn);
		this.tbl__bmk		= new Xoud_bmk_tbl(conn);
	}
	public Db_cfg_tbl				Tbl__cfg()		{return tbl__cfg;}		private final Db_cfg_tbl tbl__cfg;
	public Xoud_site_tbl			Tbl__site()		{return tbl__site;}		private final Xoud_site_tbl tbl__site;
	public Xoud_history_tbl			Tbl__history()	{return tbl__history;}	private final Xoud_history_tbl tbl__history;
	public Xof_cache_tbl			Tbl__cache()	{return tbl__cache;}	private final Xof_cache_tbl tbl__cache;
	public Xoud_bmk_tbl				Tbl__bmk()		{return tbl__bmk;}		private final Xoud_bmk_tbl tbl__bmk;
}