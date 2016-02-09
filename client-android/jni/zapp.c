#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <dirent.h>
#include <errno.h>
#include <sys/stat.h>

#include <unistd.h>

void executecmd(char* c);
int exe(int argc, char* argv[]);
int main(int argc, char* argv[])
{
    char* c;
	char* d;
	c = argv[0];
	d = "export CLASSPATH=/data/app/com.zl.movetest-2.apk && export LD_LIBRARY_PATH=/vendor/lib:/system/lib && exec app_process /data/app com.zl.movetest/MoveUtil com.zl.hw 2";
	puts("print cmd: start");
	puts(argv[1]);
	puts(argv[2]);
	puts("print cmd: end");
	printf("%s \n",argv[1]);
	printf("%s \n",argv[2]);
	/*
	puts(c);
	puts(d);
	argv[1] = "-c";
	argv[2] = d;
	executecmd(d);
	  return 0;
	  	return exe(argc,argv);
	*/
	return exe(argc,argv);
}

void executecmd(char* c){

		/*
				char Com[80];
		strcpy(Com,"netsh wlan set hostednetwork mode=allow ssid=");
		strcat(Com,u.c_str());
		strcat(Com," key=");                                      
		strcat(Com,k.c_str());
		*/
		system(c);
		
		return;
	}
	
static int executionFailure(char *context)
{
        fprintf(stderr, "su: %s. Error:%s\n", context, strerror(errno));
        return -errno;
}
static int permissionDenied()
{
        // the superuser activity couldn't be started
        printf("su: permission denied\n");
        return 1;
}

int exe(int argc, char* argv[]){
 struct stat stats;
        struct passwd *pw;
        int uid = 0;
        int gid = 0;
		
		if(setgid(gid) || setuid(uid)) 
                return permissionDenied();

        char *exec_args[argc + 1];
        exec_args[argc] = NULL;
        exec_args[0] = "sh";
        int i;
		char stra[256];
		sprintf(stra,"%d",argc);
		char coma[80];
				strcpy(coma,"main cmd num : ");
				strcat(coma,stra);
				puts(coma);
        for (i = 1; i < argc; i++)
        {
                exec_args[i] = argv[i];
				char Com[80];
				strcpy(Com,"main cmd: ");
				strcat(Com,exec_args[i]);
				puts(Com);

        }
        execv("/system/bin/sh", exec_args);
        return executionFailure("sh");

}
	
	