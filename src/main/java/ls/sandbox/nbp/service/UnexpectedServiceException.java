//------------------------------------------------------------------------------
//
// System : spring_microservices_sandbox
//
// Sub-System : ls.sandbox.nbp.service
//
// File Name : UnexpectedServiceException.java
//
// Author : Lukasz.Stochlak
//
// Creation Date : 01.07.2024
//
//------------------------------------------------------------------------------
package ls.sandbox.nbp.service;

/**
 * @author Lukasz.Stochlak
 */
public class UnexpectedServiceException extends RuntimeException
{
    public UnexpectedServiceException()
    {
        super();
    }

    public UnexpectedServiceException(String message)
    {
        super(message);
    }

    public UnexpectedServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UnexpectedServiceException(Throwable cause)
    {
        super(cause);
    }

    public UnexpectedServiceException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
//------------------------------------------------------------------------------
