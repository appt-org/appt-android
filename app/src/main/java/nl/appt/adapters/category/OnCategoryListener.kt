package nl.appt.adapters.category

import nl.appt.model.Block

interface OnCategoryListener{
    fun onCategoryClicked(block: Block)
}