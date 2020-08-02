package com.jfc.service;

public interface PackageService {
    /**
     * 领取卡券
     * @param packgeId
     * @param memberId
     * @param type
     * @throws Exception
     */
    void addUserCouponPackageInfo(String packgeId,String memberId,boolean type)throws Exception;
}
