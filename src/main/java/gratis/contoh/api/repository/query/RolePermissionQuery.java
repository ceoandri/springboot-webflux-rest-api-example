package gratis.contoh.api.repository.query;

public class RolePermissionQuery {
	
	public static final String getRolePermission = "select mr.role_desc as role, "
			+ "	mm.module_desc as module, "
			+ "	string_agg(mat.access_type_desc, ',' order by mat.access_type_desc asc) as permission "
			+ "from public.permission p "
			+ "inner join mst_role mr ON p.role_name = mr.role_name "
			+ "inner join mst_module mm ON p.module_name = mm.module_name "
			+ "inner join mst_access_type mat on p.access_type_name = mat.access_type_name "
			+ "group by mr.role_desc , mm.module_desc";
	
}
