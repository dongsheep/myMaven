package com.dong.constant;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.dong.domain.User;
import com.dong.service.AccountServiceI;
import com.dong.utils.Util;

/**
 * �Զ���Realm
 * 
 * @author xiedongxiao
 *
 */
public class MyShiroRealm extends AuthorizingRealm {

	/**
	 * ��־
	 */
	private static Logger log = LogManager.getLogger(MyShiroRealm.class);

	/** �û���֤��ҵ���� **/
	private AccountServiceI accountService;

	public AccountServiceI getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountServiceI accountService) {
		this.accountService = accountService;
	}

	/**
	 * ��ȡ��Ȩ��Ϣ
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		User user = (User) principal.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// ���ý�ɫ��Ȩ��
		// find role
		List<String> roles = accountService.getRolesByUserName(user.getName());
		for (String each : roles) {
			// ����ɫ��Դ��ӵ��û���Ϣ��
			info.addRole(each);
		}
		log.debug("User:[" + user.getName() + "] has roles " + roles.toString());
		// find permission
		List<String> pers = accountService.getPermissionsByUserName(user.getName());
		for (String each : pers) {
			// ��Ȩ����Դ��ӵ��û���Ϣ��
			info.addStringPermission(each);
		}
		log.debug("User:[" + user.getName() + "] has permissions " + pers.toString());
		return info;
	}

	/**
	 * ��ȡ��֤��Ϣ
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) at;
		// ͨ�������յ��û���
		String username = token.getUsername();
		User user = accountService.getUserByUserName(username);
		if (user != null) {
			String userPwd = user.getPassword();
			if (Util.isEmpty(new String(token.getPassword()))) {
				log.info("UMS login not need password.");
				userPwd = user.getSalt();
			}
//			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, userPwd, ByteSource.Util.bytes(user.getSalt()), getName());
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, userPwd, getName());
			return info;
		} else {
			throw new UnknownAccountException();
		}
	}

}
