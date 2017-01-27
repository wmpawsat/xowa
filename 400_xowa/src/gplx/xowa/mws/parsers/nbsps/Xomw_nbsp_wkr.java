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
package gplx.xowa.mws.parsers.nbsps; import gplx.*; import gplx.xowa.*; import gplx.xowa.mws.*; import gplx.xowa.mws.parsers.*;
import gplx.core.btries.*;
public class Xomw_nbsp_wkr {
	private final    Btrie_rv trv = new Btrie_rv();
	public void Do_nbsp(Xomw_parser_ctx pctx, Xomw_parser_bfr pbfr) {
		// PORTED:
		// Clean up special characters, only run once, next-to-last before doBlockLevels
		// $fixtags = [
		//	// French spaces, last one Guillemet-left
		//	// only if there is something before the space
		//	'/(.) (?=\\?|:|;|!|%|\\302\\273)/' => '\\1&#160;',
		//	// french spaces, Guillemet-right
		//	'/(\\302\\253) /' => '\\1&#160;',
		//	'/&#160;(!\s*important)/' => ' \\1', // Beware of CSS magic word !important, T13874.
		// ];
		// $text = preg_replace( array_keys( $fixtags ), array_values( $fixtags ), $text );
		// XO.PBFR
		Bry_bfr src_bfr = pbfr.Src();
		byte[] src = src_bfr.Bfr();
		int src_bgn = 0;
		int src_end = src_bfr.Len();
		Bry_bfr bfr = pbfr.Trg();

		if (trie == null) {
			synchronized (this.getClass()) {
				trie = Btrie_slim_mgr.cs();
				Trie__add(trie, Tid__space_lhs, " ?");
				Trie__add(trie, Tid__space_lhs, " :");
				Trie__add(trie, Tid__space_lhs, " ;");
				Trie__add(trie, Tid__space_lhs, " !");
				Trie__add(trie, Tid__space_lhs, " »");
				Trie__add(trie, Tid__space_rhs, "« ");
				Trie__add(trie, Tid__important, "&#160;!");
			}
		}

		int cur = src_bgn;
		int prv = cur;
		boolean dirty = true;
		// search forward for...
		//   "\s" before ? : ; ! % 302,273; EX: "a :"
		//   "\s" after 302,253
		//   "&160;!\simportant"			
		while (true) {
			if (cur == src_end) {
				if (dirty)
					bfr.Add_mid(src, prv, src_end);
				break;
			}
			Object o = trie.Match_at(trv, src, cur, src_end);
			if (o == null) {
				cur++;
				continue;
			}
			Xomw_nbsp_itm itm = (Xomw_nbsp_itm)o;

			// '/&#160;(!\s*important)/' => ' \\1'
			byte itm_tid = itm.Tid();
			int important_end = -1;
			if (itm_tid == Tid__important) {
				int space_bgn = cur + itm.Key().length;
				int space_end = Bry_find_.Find_fwd_while(src, space_bgn, src_end, Byte_ascii.Space);
				important_end = space_end + Bry__important.length;
				if (!Bry_.Match(src, space_end, important_end, Bry__important)) {
					continue;
				}
			}
			dirty = true;
			bfr.Add_mid(src, prv, cur);
			switch (itm_tid) {
				case Tid__space_lhs:
					bfr.Add_bry_many(Bry__nbsp, itm.Val());
					break;
				case Tid__space_rhs:
					bfr.Add_bry_many(itm.Val(), Bry__nbsp);
					break;
				case Tid__important:
					bfr.Add(Bry__important__repl);
					break;
			}
			cur += itm.Key().length;
			prv = cur;
		}
		if (dirty)
			pbfr.Switch();
	}
	private static final byte Tid__space_lhs = 0, Tid__space_rhs = 1, Tid__important = 2;
	private static Btrie_slim_mgr trie;
	private static void Trie__add(Btrie_slim_mgr trie, byte tid, String key_str) {
		byte[] key_bry = Bry_.new_u8(key_str);
		byte[] val_bry = null;
		switch (tid) {
			case Tid__space_lhs:
				val_bry = Bry_.Mid(key_bry, 1);
				break;
			case Tid__space_rhs:
				val_bry = Bry_.Mid(key_bry, 0, key_bry.length - 1);
				break;
			case Tid__important:
				val_bry = key_bry;
				break;
		}
		Xomw_nbsp_itm itm = new Xomw_nbsp_itm(tid, key_bry, val_bry);
		trie.Add_obj(key_bry, itm);
	}
	private static final    byte[] Bry__nbsp = Bry_.new_a7("&#160;"), Bry__important = Bry_.new_a7("important"), Bry__important__repl = Bry_.new_a7(" !");
}
class Xomw_nbsp_itm {
	public Xomw_nbsp_itm(byte tid, byte[] key, byte[] val) {
		this.tid = tid;
		this.key = key;
		this.val = val;
	}
	public byte Tid() {return tid;} private final    byte tid;
	public byte[] Key() {return key;} private final    byte[] key;
	public byte[] Val() {return val;} private final    byte[] val;
}