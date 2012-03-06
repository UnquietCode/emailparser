;NSIS Modern User Interface version 1.70
;Rename Installer Script
;Written by zhangzuoqiang

;--------------------------------
;Include Modern UI

  !include "MUI.nsh"

;--------------------------------
;General

  ;Name and file
  Name "Rename"
  OutFile "RenameInstaller.exe"

  ;Default installation folder
  InstallDir "$PROGRAMFILES\SZ-AISTOR\Rename"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\Rename" ""

;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING
    !define MUI_HEADERIMAGE "E:\Spaces\Renommeur\installer\install.bmp"
    !define MUI_HEADERIMAGE_BITMAP_NOSTRETCH
    !define MUI_HEADERIMAGE_BITMAP "E:\Spaces\Renommeur\installer\install.bmp"
    !define MUI_ICON "E:\Spaces\Renommeur\installer\setup.ico"
    !define MUI_UNICON "E:\Spaces\Renommeur\installer\unsetup.ico"

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_LICENSE "E:\Spaces\Renommeur\installer\License.txt"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections

Section "Rename (required)" SecDummy

  SectionIn RO

  ;Files to be installed
  SetOutPath "$INSTDIR"
  
   File "E:\Spaces\Renommeur\Rename_0.9.jar"
   File "E:\Spaces\Renommeur\Res\rename_32.ico"

    SetOutPath "$INSTDIR\Renommeur_lib"

    File "E:\Spaces\Renommeur\Renommeur_lib\appframework-1.0.3.jar"
    File "E:\Spaces\Renommeur\Renommeur_lib\metadata-extractor-2.4.0-beta-1.jar"
    File "E:\Spaces\Renommeur\Renommeur_lib\swing-worker-1.1.jar"

  SetOutPath "$INSTDIR"

    ; Write the installation path into the registry
  WriteRegStr HKLM SOFTWARE\Rename "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename" "DisplayName" "Rename"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
  
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"
  CreateDirectory "$SMPROGRAMS\Rename"
  CreateShortCut "$SMPROGRAMS\Rename\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe"
  CreateShortCut "$SMPROGRAMS\Rename\Rename.lnk" "$INSTDIR\Rename_0.9.jar" "" "$INSTDIR\rename_32.ico"
SectionEnd

;--------------------------------
;Uninstaller Section

Section "Uninstall"

  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename"
  DeleteRegKey HKLM SOFTWARE\Rename
  DeleteRegKey /ifempty HKCU "Software\Rename"

    ; Remove shortcuts
  RMDir /r "$SMPROGRAMS\Rename"

  ; Remove directories used
  RMDir /r "$INSTDIR"

SectionEnd