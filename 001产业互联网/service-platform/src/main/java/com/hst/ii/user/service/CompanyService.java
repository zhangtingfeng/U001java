//package com.hst.ii.user.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.transaction.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.ui.Model;
//
//import com.hst.core.dao.IORDao;
//import com.hst.core.dao.ISQLDao;
//import com.hst.core.dao.ORQuery;
//import com.hst.core.dao.ORQuery.Op;
//import com.hst.ii.user.entity.TOrgInfo;
//import com.hst.ii.user.entity.TUser;
//import com.hst.ii.user.entity.TUserRole;
//
//@Service
//@Transactional
//public class CompanyService {
//
//	@Autowired
//	ISQLDao sql;
//
//	@Autowired
//	IORDao dao;
//
//	/**
//	 * 审核
//	 * @param companylogs
//	 * @param op: PASS/BACK
//	 * @param reamrk 备注
//	 * @param m
//	 */
//	public void approve(List<TCompanyLog> companylogs,String op,String remark,Model m) {
//		for(TCompanyLog clog:companylogs) {
//			clog.setRemark(remark);
//
//			if("PASS".equals(op)) {
//				clog.setStatus("3");
//				//通过后修改 机构状态和 用户状态
//				TOrgInfo org = dao.list(TOrgInfo.class,new ORQuery(Op.eq,"code", clog.getOrgcode())).get(0);
//				org.setStatus("3");
//				org.setName(clog.getBankAccountName());
//				dao.update(org);
//
//				//更改账户状态 给予管理员角色
//				List<ORQuery> querys = new ArrayList<ORQuery>();
//				querys.add(new ORQuery(Op.eq,"orgcode",clog.getOrgcode()));
//				List<TUser> users = dao.list(TUser.class,querys);
//				if(users.size() > 0) {
//					TUser user = users.get(0);
//					user.setType("1");
//					dao.update(user);
//
//					//修改用户角色为 机构管理员
//					querys = new ArrayList<ORQuery>();
//					querys.add(new ORQuery(Op.eq,"userid",user.getId()));
//					List<TUserRole> urs = dao.list(TUserRole.class,querys);
//					if(urs.size() >0 ) {
//						dao.delete(urs.get(0));
//					}
//					TUserRole ur = new TUserRole();
//					ur.setUserid(user.getId());
//					ur.setRoleid("orgadmin");
//					dao.save(ur);
//				}
//				// 保存到正式
//				this.saveToCompany(clog);
//			}else if("BACK".equals(op)) {
//				if("1".equals(clog.getStatus())){
//					clog.setStatus("2");
//				}else if("5".equals(clog.getStatus())) {
//					clog.setStatus("5");
//				}
//			}
//			dao.update(clog);
//		}
//		m.addAttribute("success","success");
//	}
//
//	/**
//	 *  认证/变更申请 通过后，把信息移动到正式表中
//	 * @param clog
//	 */
//	private TCompany saveToCompany(TCompanyLog clog) {
//		List<TCompany> lists = dao.list(TCompany.class, new ORQuery(Op.eq, "orgcode", clog.getOrgcode()));
//		TCompany company = lists.size()>0 ? lists.get(0) : new TCompany();
//		company.setCompanyName(clog.getCompanyName());
//		company.setCompanyCode(clog.getCompanyCode());
//		company.setAdminName(clog.getAdminName());
//		company.setAdminIdNo(clog.getAdminIdNo());
//		company.setAdminTel(clog.getAdminTel());
//		company.setCompanyType(clog.getCompanyType());
//		company.setAddress(clog.getAddress());
//		company.setRealAddress(clog.getRealAddress());
//		company.setPostcode(clog.getPostcode());
//		company.setSocialCreditCode(clog.getSocialCreditCode());
//		company.setPhone(clog.getPhone());
//		company.setFax(clog.getFax());
//		company.setStampMaker(clog.getStampMaker());
//		company.setStampMakerMoblie(clog.getStampMakerMoblie());
//		company.setStampMakerMail(clog.getStampMakerMail());
//		company.setBank(clog.getBank());
//		company.setBankBranch(clog.getBankBranch());
//		company.setBankAccountName(clog.getBankAccountName());
//		company.setBankAccount(clog.getBankAccount());
//		company.setLegalPersonName(clog.getLegalPersonName());
//		company.setLegalPaperType(clog.getLegalPaperType());
//		company.setLegalPaperNumber(clog.getLegalPaperNumber());
//		company.setLegalPaperPhone(clog.getLegalPaperPhone());
//		company.setLegalPaperMobile(clog.getLegalPaperMobile());
//		company.setStatus(clog.getStatus());
//		company.setOrgcode(clog.getOrgcode());
//		company.setCertificatesOne(clog.getCertificatesOne());
//		company.setPic1(clog.getPic1());
//		company.setPic2(clog.getPic2());
//		company.setPic3(clog.getPic3());
//		TCompany c = lists.size()>0 ? dao.update(company) : dao.save(company);
//		return c;
//	}
//
//	/**
//	 *  锁定/解锁用户
//	 * @param params
//	 * @param type 0：解锁  1：锁定
//	 * @param m
//	 */
//	public void locked(Map<String,Object> params, String type, Model m) {
//		String id = params.get("id").toString();
//		TCompany  c = dao.get(TCompany.class, id);
//		if(null == c) return ;
//		c.setLocked(type);
//		dao.update(c);
//	}
//}
