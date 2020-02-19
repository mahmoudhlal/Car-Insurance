package com.mahmoud.carsinsurance.Utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.mahmoud.carsinsurance.R
import org.passay.*
import java.util.*

class Validator {
    /*global field validator for this app*/
    companion object {
        private val EMAIL_VERIFICATION =
            "^([\\w-.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$"
        private val SAUDI_PHONE_VERIFICATION = "^05[\\d]{8}$"
        private val EGYPT_PHONE_VERIFICATION = "^01[\\d]{9}$"

        /*this function validates a array of edit text */
        fun validateInputField(
            array: Array<TextInputEditText>,
            context: Activity
        ): Boolean {

            var count = 0
            for (editText in array) {
                if (editText.text.toString().isEmpty()) {
                    Toast.makeText(
                        context,
                        editText.tag.toString() + context.resources.getString(R.string.is_empty),
                        Toast.LENGTH_LONG
                    ).show()
                    break
                } else {
                    if (editText.tag.toString() == context.resources.getString(R.string.email)) {
                        if (editText.text.toString().trim { it <= ' ' }.matches(
                                Regex(
                                    EMAIL_VERIFICATION
                                )
                            )
                        ) {
                            count++
                        } else {
                            editText.error =
                                editText.tag.toString() + " " + context.resources.getString(
                                    R.string.is_invalid
                                )
                            Toast.makeText(
                                context,
                                editText.tag.toString() + " " + context.resources.getString(R.string.is_invalid),
                                Toast.LENGTH_SHORT
                            ).show()
                            break
                        }
                    } /*else if(editText.getTag().toString().equals(context.getResources().getString(R.string.phone))) {
                    if (editText.getText().toString().trim().matches(EGYPT_PHONE_VERIFICATION))
                        count++;
                    else
                        Toast.makeText(context, editText.getTag().toString()+" "+ context.getResources().getString(R.string.is_invalid), Toast.LENGTH_SHORT).show();
                }*/ /*else if (editText.tag.toString() == context.resources.getString(R.string.password)) {
                        val error: String =
                            passwordValidator(
                                context,
                                8,
                                editText.text.toString().trim { it <= ' ' }
                            )
                        if (!error.isEmpty()) Toast.makeText(
                            context,
                            error,
                            Toast.LENGTH_SHORT
                        ).show() else count++*/
                     else count++
                }
            }
            return array.size == count
        }


        private fun passwordValidator(
            context: Context,
            minLength: Int,
            password: String
        ): String {
            val lengthRule = LengthRule()
            lengthRule.setMinimumLength(minLength)
            val passwordValidator = PasswordValidator(
                lengthRule,  // length between 8 and 16 characters
                CharacterRule(
                    EnglishCharacterData.UpperCase,
                    1
                ),  // at least one upper-case character
/*new CharacterRule(EnglishCharacterData.LowerCase, 1),// at least one lower-case character*/
                CharacterRule(EnglishCharacterData.Digit, 1),  // at least one digit character
                CharacterRule(
                    EnglishCharacterData.Special,
                    1
                ),  // at least one symbol (special character)
                WhitespaceRule() // no whitespace
            )
            val resultDetails: List<RuleResultDetail>
            resultDetails =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) passwordValidator.validate(
                    PasswordData(password)
                ).getDetails() else ArrayList<RuleResultDetail>()
            return if (resultDetails.size == 0) "" else {
                when (resultDetails[0].getErrorCode()) {
                    "TOO_SHORT" -> context.resources.getString(R.string.password) + " " + context.resources.getString(
                        R.string.is_weak
                    ) + ", " + context.resources.getString(R.string.must_be_eight)
                    "INSUFFICIENT_DIGIT" -> context.resources.getString(R.string.password) + " " + context.resources.getString(
                        R.string.is_weak
                    ) + ", " + context.resources.getString(R.string.must_contain_numbers)
                    "INSUFFICIENT_UPPERCASE" -> context.resources.getString(R.string.password) + " " + context.resources.getString(
                        R.string.is_weak
                    ) + ", " + context.resources.getString(R.string.must_contain_capital)
                    "INSUFFICIENT_SPECIAL" -> context.resources.getString(R.string.password) + " " + context.resources.getString(
                        R.string.is_weak
                    ) + ", " + context.resources.getString(R.string.must_contain_special)
                    "ILLEGAL_WHITESPACE" -> context.resources.getString(R.string.password) + " " + context.resources.getString(
                        R.string.is_invalid
                    ) + ", " + context.resources.getString(R.string.spaces_not_allowed)
                    else -> ""
                }
            }
        }

    }
}