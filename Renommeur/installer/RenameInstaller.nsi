;NSIS Modern User Interface version 1.70
;Rename Installer Script
;Written by zhangzuoqiang

;--------------------------------
; 宏定义

 !define PRODUCT_VERSION "0.9.8.1"
 !define PRODUCT_NAME "Rename"
 !define PRODUCT_COPYRIGHT "Copyright @ SZ-AISTOR"
 !define PRODUCT_COMPANY "SHENZHEN AISTOR IST CO.,LTD."
 !define PRODUCT_COMMENTS "Photo rename a management tool."
 !define PRODUCT_VENDOR "zhangzuoqiang"
 
 !define JRE_VERSION "1.6.0" 
 !define JRE_URL "http://emailparser.googlecode.com/files/jre-6u31-windows-i586.exe"

;--------------------------------
;Include Modern UI

  !include "MUI.nsh"
  !include "nsDialogs.nsh"
  !include "JREDyna.nsh"
;--------------------------------
;General
 
  ;Name and file
  Name "${PRODUCT_NAME}"
  OutFile "RenameInstaller.exe"
  AutoCloseWindow false
  ShowInstDetails show

  ;Default installation folder
  InstallDir "$PROGRAMFILES\SZ-AISTOR\Rename"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\Rename" ""

;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING
  !define MUI_HEADERIMAGE "E:\Spaces\Renommeur\installer\install.bmp"
  !define MUI_HEADERIMAGE_BITMAP_NOSTRETCH
  !define MUI_HEADERIMAGE_BITMAP "E:\Spaces\Renommeur\installer\install.bmp" ;顶部左侧图片
  !define MUI_ICON "E:\Spaces\Renommeur\installer\setup.ico" ;安装包图标
  !define MUI_UNICON "E:\Spaces\Renommeur\installer\unsetup.ico" ;卸载文件图标

;--------------------------------
;Pages

  ; //
  !insertmacro CUSTOM_PAGE_JREINFO
  
  !insertmacro MUI_PAGE_LICENSE "E:\Spaces\Renommeur\installer\License.txt"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "SimpChinese"
  !insertmacro MUI_LANGUAGE "English"
  
  VIProductVersion "${PRODUCT_VERSION}"
  VIAddVersionKey "FileVersion" "${PRODUCT_VERSION}"
  VIAddVersionKey "FileDescription" "${PRODUCT_COMMENTS}"
  VIAddVersionKey "LegalCopyright" "${PRODUCT_COPYRIGHT}"
  VIAddVersionKey "ProductName" "${PRODUCT_NAME}"
  VIAddVersionKey "Comments" "${PRODUCT_COMMENTS}"
  VIAddVersionKey "CompanyName" "${PRODUCT_COMPANY}"

;--------------------------------
;Installer Sections

Section "Rename" SecDummy
    
  SectionIn RO

  ;Files to be installed
  SetOutPath "$INSTDIR"
  File "E:\Spaces\Renommeur\Rename_0.9.jar"
  File "E:\Spaces\Renommeur\Res\rename.ico"
  File "E:\Spaces\Renommeur\installer\runjar.bat"
  
  SetOutPath "$INSTDIR\Renommeur_lib"
  File "E:\Spaces\Renommeur\Renommeur_lib\appframework-1.0.3.jar"
  File "E:\Spaces\Renommeur\Renommeur_lib\metadata-extractor-2.4.0-beta-1.jar"
  File "E:\Spaces\Renommeur\Renommeur_lib\swing-worker-1.1.jar"
  
  SetOutPath "$INSTDIR"

    ; Write the installation path into the registry
  WriteRegStr HKLM SOFTWARE\Rename "Install_Dir" "$INSTDIR"
  
  WriteRegStr HKEY_LOCAL_MACHINE SOFTWARE\Classes\.jar "" jar_auto_file
  WriteRegStr HKEY_LOCAL_MACHINE SOFTWARE\Classes\jar_auto_file "" ""
  WriteRegStr HKEY_LOCAL_MACHINE SOFTWARE\Classes\jar_auto_file\shell "" open
  WriteRegStr HKEY_LOCAL_MACHINE SOFTWARE\Classes\jar_auto_file\shell\open "" ""
  DeleteRegValue HKEY_LOCAL_MACHINE SOFTWARE\Classes\jar_auto_file\shell\open ""
  WriteRegStr HKEY_LOCAL_MACHINE SOFTWARE\Classes\jar_auto_file\shell\open\command "" "$\"$WINDIR\runjar.bat$\" $\"%1$\""
  ; http://blog.csdn.net/hqcchina/article/details/4389685
  
  
  
  
  
  
  
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename" "DisplayName" "${PRODUCT_NAME}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename" "Publisher" "${PRODUCT_COMPANY}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
SectionEnd

Section "Install Java JRE"
  SectionIn RO
  call DownloadAndInstallJREIfNecessary
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"
  CreateDirectory "$SMPROGRAMS\Rename"
  CreateShortCut "$SMPROGRAMS\Rename\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe"
  CreateShortCut "$SMPROGRAMS\Rename\Rename.lnk" "$INSTDIR\Rename_0.9.jar" "" "$INSTDIR\rename.ico"
SectionEnd

Section "Desktop shortcut"
  CreateShortCut "$DESKTOP\Rename.lnk" "$INSTDIR\Rename_0.9.jar" "" "$INSTDIR\rename.ico"
SectionEnd

;--------------------------------
;Uninstaller Section

Section "Uninstall"

  ; Remove shortcuts
  Delete /REBOOTOK "$SMPROGRAMS\Rename\Uninstall.lnk"
  Delete /REBOOTOK "$SMPROGRAMS\Rename\Rename.lnk"
  RMDir "$SMPROGRAMS\Rename"
  
  Delete /REBOOTOK "$DESKTOP\Rename.lnk"

  ; Remove directories used
  RMDir /r "$INSTDIR"
  RMDir /r "$PROGRAMFILES\SZ-AISTOR"

  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Rename"
  DeleteRegKey HKLM SOFTWARE\Rename
  DeleteRegKey /ifempty HKCU "Software\Rename"
  
  SetAutoClose false

SectionEnd