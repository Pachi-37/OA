package entity;

public class RoleUser {
    private Long rnId;
    private Long roleId;
    private Long userId;

    public Long getRnId() {
        return rnId;
    }

    public void setRnId(Long rnId) {
        this.rnId = rnId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
