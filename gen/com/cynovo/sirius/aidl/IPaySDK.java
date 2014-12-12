/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\Eclipse\\JobWorkSpace\\CloudPos_PAR10\\src\\com\\cynovo\\sirius\\aidl\\IPaySDK.aidl
 */
package com.cynovo.sirius.aidl;
public interface IPaySDK extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.cynovo.sirius.aidl.IPaySDK
{
private static final java.lang.String DESCRIPTOR = "com.cynovo.sirius.aidl.IPaySDK";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.cynovo.sirius.aidl.IPaySDK interface,
 * generating a proxy if needed.
 */
public static com.cynovo.sirius.aidl.IPaySDK asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.cynovo.sirius.aidl.IPaySDK))) {
return ((com.cynovo.sirius.aidl.IPaySDK)iin);
}
return new com.cynovo.sirius.aidl.IPaySDK.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_payCash:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.payCash(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_registerPOS:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.registerPOS(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getPOSInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getPOSInfo(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getHardWareState:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getHardWareState();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.cynovo.sirius.aidl.IPaySDK
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String payCash(java.lang.String jsonData) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jsonData);
mRemote.transact(Stub.TRANSACTION_payCash, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String registerPOS(java.lang.String jsonData) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jsonData);
mRemote.transact(Stub.TRANSACTION_registerPOS, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getPOSInfo(java.lang.String jsonData) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jsonData);
mRemote.transact(Stub.TRANSACTION_getPOSInfo, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getHardWareState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getHardWareState, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_payCash = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_registerPOS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getPOSInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getHardWareState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public java.lang.String payCash(java.lang.String jsonData) throws android.os.RemoteException;
public java.lang.String registerPOS(java.lang.String jsonData) throws android.os.RemoteException;
public java.lang.String getPOSInfo(java.lang.String jsonData) throws android.os.RemoteException;
public java.lang.String getHardWareState() throws android.os.RemoteException;
}
