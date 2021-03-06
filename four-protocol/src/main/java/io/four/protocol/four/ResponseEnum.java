package io.four.protocol.four;

/**
 * @author TheLudlows
 * @since 0.1
 */
public interface ResponseEnum {

    byte SUCCESS = 0x20;

    byte IMPL_ERROR = 0x50;

    byte NO_IMPL = 0x51;
   /* CLIENT_ERROR((byte) 0x30,"CLIENT_ERROR"),                   // 内部错误 — 因为意外情况, 客户端不能发送请求

    CLIENT_TIMEOUT((byte) 0x31,"CLIENT_TIMEOUT"),                 // 超时 - 客户端超时

    SERVER_TIMEOUT((byte) 0x32,"SERVER_TIMEOUT"),                 // 超时 - 服务端超时

    BAD_REQUEST((byte) 0x40,"BAD_REQUEST"),                    // 错误请求 — 请求中有语法问题, 或不能满足请求

    SERVICE_NOT_FOUND((byte) 0x44,"SERVICE_NOT_FOUND"),              // 找不到 - 指定服务不存在

    SERVER_ERROR((byte) 0x50,"SERVER_ERROR"),                   // 内部错误 — 因为意外情况, 服务器不能完成请求

    SERVER_BUSY((byte) 0x51,"SERVER_BUSY"),      // 内部错误 — 服务器太忙, 无法处理新的请求

    SERVICE_ERROR((byte) 0x52,"SERVICE_ERROR"),                  // 服务错误 - 服务执行意外出错

    APP_FLOW_CONTROL((byte) 0x53,"APP_FLOW_CONTROL"),               // 服务错误 - App级别服务限流

    PROVIDER_FLOW_CONTROL((byte) 0x54,"PROVIDER_FLOW_CONTROL");          // 服务错误 - Provider级别服务限流
*/

}

