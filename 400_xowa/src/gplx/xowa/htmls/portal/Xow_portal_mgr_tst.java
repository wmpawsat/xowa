/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2017 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.htmls.portal; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*;
import org.junit.*;
public class Xow_portal_mgr_tst {
	@Before public void init() {fxt.Init();} private Xowh_portal_mgr_fxt fxt = new Xowh_portal_mgr_fxt();
	@Test  public void Div_ns_bry() {
		fxt.Test_div_ns_bry("A"			, "/wiki/A;selected;/wiki/Talk:A;xowa_display_none;");
		fxt.Test_div_ns_bry("Talk:A"	, "/wiki/A;;/wiki/Talk:A;selected;");
	}
	@Test  public void Div_personal_bry() {
		fxt.Test_div_personal_bry("/wiki/User:anonymous;anonymous;xowa_display_none;/wiki/User_talk:anonymous;xowa_display_none;");
	}
	@Test  public void Missing_ns_cls() {
		fxt.Test_missing_ns_cls("xowa_display_none");
	}
}
class Xowh_portal_mgr_fxt {
	public void Init() {
		if (app == null) {
			app = Xoa_app_fxt.Make__app__edit();
			wiki = Xoa_app_fxt.Make__wiki__edit(app);
			wiki.Ns_mgr().Ns_main().Exists_(true);	// needed for ns
			wiki.Html_mgr().Portal_mgr().Init_assert();	// needed for personal
			wiki.Html_mgr().Portal_mgr().Missing_ns_cls_(Bry_.new_a7("xowa_display_none"));
		}
	}	private Xoae_app app; Xowe_wiki wiki;
	public void Test_div_ns_bry(String ttl, String expd) {
		Tfds.Eq(expd, String_.new_a7(wiki.Html_mgr().Portal_mgr().Div_ns_bry(wiki.Utl__bfr_mkr(), Xoa_ttl.Parse(wiki, Bry_.new_a7(ttl)), wiki.Ns_mgr())));
	}
	public void Test_div_personal_bry(String expd) {
		Tfds.Eq(expd, String_.new_a7(wiki.Html_mgr().Portal_mgr().Div_personal_bry()));
	}
	public void Test_missing_ns_cls(String expd) {
		Tfds.Eq(expd, String_.new_a7(wiki.Html_mgr().Portal_mgr().Missing_ns_cls()));
	}
}
