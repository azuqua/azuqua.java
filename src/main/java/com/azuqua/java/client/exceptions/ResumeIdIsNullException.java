package com.azuqua.java.client.exceptions;

/**
 * <p>
 *     Exception is thrown when the user is attempting to execute Flo#resume method. This
 *     exception indicates that the flo resume id wasn't set in the Flo object.
 * </p>
 *
 * <p>
 *     To set the id, you will you need to invoke a flo that has flo-resume capabilities.
 *     When the Flo#invoke method is called, the resume id will be saved as an instance
 *     property. This id will be use when the Flo#resume method is called.
 * </p>
 *
 * <p>
 *     Created by quyle on 11/9/15.
 * </p>
 */
public class ResumeIdIsNullException extends Throwable {
}
