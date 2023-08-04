package cn.zhumingwu.client.enums;

public enum GrantType {
    Implicit,
    Hybrid,
    AuthorizationCode, // ��ã�ͨ����վ�����û���Ȩ��ת
    ClientCredentials, // û����վʱ�����û���ȡ��Ȩ����д
    ResourceOwnerPassword,
    DeviceFlow
}