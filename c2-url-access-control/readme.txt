通过拦截访问的链接来判断是否有权限访问.
通过 AccessDecisionManager 的几个投票者判定资源是否可被访问, URL 权限控制实现在其中的一个RoleBasedVoter 中, 通过动态的获取这个用户/ 角色可以访问的URL 来判断拦截的URL 是否可以访问.