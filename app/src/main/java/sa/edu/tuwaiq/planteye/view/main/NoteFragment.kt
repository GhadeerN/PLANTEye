package sa.edu.tuwaiq.planteye.view.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import sa.edu.tuwaiq.planteye.R
import sa.edu.tuwaiq.planteye.databinding.FragmentNoteBinding
import sa.edu.tuwaiq.planteye.model.PlantDataModel
import sa.edu.tuwaiq.planteye.view.FILE_NAME
import sa.edu.tuwaiq.planteye.view.USER_ID

private const val TAG = "NoteFragment"

class NoteFragment : Fragment() {

    // Get the Plant info view model -> why? to observe the plant data and save it with the note
    private val savedPlantViewModel: SavedPlantsViewModel by activityViewModels()

    private val noteViewModel: NoteViewModel by activityViewModels()

    lateinit var binding: FragmentNoteBinding

    // Shared preference to get the user id for the note update
    private lateinit var sharedPref: SharedPreferences

    lateinit var plantInfo: PlantDataModel
    lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = requireActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        userId = sharedPref.getString(USER_ID, "")!!
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        val inputTextLayout: TextInputLayout = binding.outlinedTextFieldSavedNote
        inputTextLayout.editText?.doOnTextChanged { text, start, before, count ->
            if (count > 0) {
                Log.d(TAG, "Inside doOnTextChanged - counter: $count")
                Log.d(TAG, "Inside doOnTextChanged - text: $text")
                binding.saveNoteEditButton.visibility = View.VISIBLE
            } else {
                Log.d(TAG, "else part - counter $count")
                binding.saveNoteEditButton.visibility = View.GONE
            }
        }

        binding.saveNoteEditButton.visibility = View.GONE

        // Save note, or save the edited note
        binding.saveNoteEditButton.setOnClickListener {
            noteViewModel.removeNote(userId, plantInfo)
        }

        // Listen to the change on the edit text - if it's not blank,
        var counter = 0
        //savedDetails_user_note_textInput
//        binding.savedDetailsUserNoteTextInput.addTextChangedListener {
//            object : TextWatcher {
//                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
////                    counter = 0
//                }
//
//                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                }
//
//                override fun afterTextChanged(p0: Editable?) {
//                    /* Here the counter will be initially 0, but when the user click on the editText for the first time it will be counted as a change
//                       and increment the counter. So we need to omit the first interaction with the edit text by setting the condition to counter > 1 */
//                    counter++
//                    if (counter > 1) {
//                        binding.saveNoteEditButton.visibility = View.VISIBLE
//                        Log.d(TAG, "Inside if - counter $counter")
//                        // Save note, or save the edited note
//                        binding.saveNoteEditButton.setOnClickListener {
//                            val userId = sharedPref.getString(USER_ID, "")
//                            Log.d(TAG, "User id: $userId")
//                            if (userId.isNullOrBlank()) {
//                                plantInfo.note = binding.savedDetailsUserNoteTextInput.text.toString()
//                                noteViewModel.updateNote(userId!!, plantInfo)
//                            }
//                        }
//                    } else {
//                        Log.d(TAG, "else part - counter $counter")
//                        binding.saveNoteEditButton.visibility = View.GONE
//                    }
//                }
//            }
//        }
    }

    private fun observer() {
        // Selected Plant Info
        savedPlantViewModel.selectedPlantInfo.observe(viewLifecycleOwner, { plant ->
            Log.d(TAG, "Selected Plant: $plant")
            plantInfo = plant
            if (plant.note.isNotBlank()) {
                binding.savedDetailsUserNoteTextInput.setText(
                    plant.note
                )
            }
        })

        // Success - note added
        noteViewModel.noteLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(
                    requireActivity(),
                    "Your note is added successfully",
                    Toast.LENGTH_SHORT
                ).show()
                noteViewModel.noteLiveData.postValue(null)
            }
        })

        // Remove plant first, then add it again to avoid redundancy
        noteViewModel.removeNoteLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Log.d(TAG, "Remove note live data - User id: $userId")
                if (it == "success") {
                    Log.d(TAG, "Add note HERE!!!")
                    plantInfo.note = binding.savedDetailsUserNoteTextInput.text.toString().trim()
                    noteViewModel.updateNote(userId, plantInfo)
                }
                noteViewModel.removeNoteLiveData.postValue(null)
            }
        })

        // Fail - note not added
        noteViewModel.noteErrorLiveData.observe(viewLifecycleOwner, {
//            it?.let {
                Toast.makeText(requireActivity(), "Sorry: $it", Toast.LENGTH_LONG).show()
//                noteViewModel.noteErrorLiveData.postValue(null)
//            }
        })
    }
}