package com.nopassword.common.model;

/**
 * NoPassword authentication response statuses
 *
 * @author NoPassword
 */
public enum AuthStatus {

    Alert,
    Denied,
    LogError,
    Failure,
    InvalidUser,
    Success,
    UnpairedUser,
    WaitingForResponse;

}
