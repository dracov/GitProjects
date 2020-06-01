dim filesys, newfolder, newfolderpath
Dim WshShell, strCurDir
dim folder

Set WshShell = CreateObject("WScript.Shell")

strCurDir    = WshShell.CurrentDirectory

current_date = CStr(Day(Date()))+"_"+CStr(Month(Date()))+"_"+CStr(Year(Date()))
folder= InputBox("Enter name of the project: ", "Enter name of the project")

If IsEmpty(folder) Then
	'canceled operation
	Else
		folder_name= current_date+"_"+folder
		newfolderpath = strCurDir+"\"+folder_name
		set filesys=CreateObject("Scripting.FileSystemObject")
		If Not filesys.FolderExists(newfolderpath) Then
			Set newfolder = filesys.CreateFolder(newfolderpath)
			filesys.CopyFile "C:\Users\aldmendo\Documents\z_Formatos\KRSChecklist\KRSChecklist_v4.0.xlsm",newfolderpath+"\"
		End If
End If

