package com.cool.request.ui.dsl

import com.cool.request.ui.dsl.layout.DialogPanel
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.*
import com.intellij.openapi.util.ClearableLazyValue
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.NlsContexts
import org.jetbrains.annotations.NonNls
import javax.swing.JComponent

abstract class BoundConfigurable(
    @NlsContexts.ConfigurableName private val displayName: String,
    @NonNls private val helpTopic: String? = null
) : DslConfigurableBase(), Configurable {
    override fun getDisplayName(): String = displayName
    override fun getHelpTopic(): String? = helpTopic
}

/**
 * @see DialogPanelUnnamedConfigurableBase
 */
abstract class DslConfigurableBase : UnnamedConfigurable {
    protected var disposable: Disposable? = null
        private set

    private val panel = object : ClearableLazyValue<DialogPanel>() {
        override fun compute(): DialogPanel {
            if (disposable == null) {
                disposable = Disposer.newDisposable()
            }
            val panel = createPanel()
            panel.registerValidators(disposable!!)
            return panel
        }
    }

    abstract fun createPanel(): DialogPanel

    final override fun createComponent(): JComponent = panel.value

    override fun isModified(): Boolean = panel.value.isModified()

    override fun reset() {
        panel.value.reset()
    }

    override fun apply() {
        panel.value.apply()
    }
    fun  a (): JComponent? {
        return null
    }

    override fun disposeUIResources() {
        disposable?.let {
            Disposer.dispose(it)
            disposable = null
        }

        panel.drop()
    }
}

abstract class BoundSearchableConfigurable(@NlsContexts.ConfigurableName displayName: String, helpTopic: String, private val _id: String = helpTopic)
    : BoundConfigurable(displayName, helpTopic), SearchableConfigurable {
    override fun getId(): String = _id
}