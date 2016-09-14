#/usr/bin/env python
# -*- coding: utf-8 -*-
import sys,re
from datetime import datetime
from fabric.api import *
from fabric.contrib.console import confirm



#测试服务器host1
testsvr='root@121.201.69.135:22'
#正式服务器host2
deploysvr='tiancaiw@115.159.125.75:22'
deploysvrfade='tiancaiw@115.158.124.75:22'
REMOTE_BASE_DIR=r'/var'

RE_FILES = re.compile('\r?\n')

env.hosts=[testsvr,deploysvr]


#roless
env.roledefs={'testsvr':[testsvr],'deploysvr':[deploysvr]}
env.passwords={deploysvr:"Nicajonh007"}
env.key_filename="~/.ssh/hhsvrtest"
API_SERVER=['appstore_api','stat_api','updatesys_api','iosappstore_api']

remote_dist_dir=''#测试服务器网站地址
remote_sdist_dir=''#正式服务器网站地址


sudouser="root"#sudo执行用户

def pack():
    '定义一个打包任务'
    #打一个tar包
    tar_files={'*.py','appstore_api/*','conf/*','stat_api/*','updatesys_api/*','iosappstore_api/*'}
    local('rm -rf www-svr.tar.gz')
    local('chmod -R 755 ./*')
    local('tar -czvf www-svr.tar.gz --exclude=\'*.tar.gz\' --exclude=\'fabfile.py\' --exclude=\'*.pyc\' --exclude=\'.git/*\' --exclude=\'.idea/*\' %s' % ' '.join(tar_files))

@roles('testsvr')
def rollbacktest():
    """
    回退到一个版本
    :return:
    """
    with cd(REMOTE_BASE_DIR):
        r=run('ls -p -l')
        files=[s[:-1] for s in RE_FILES.split(r) if s.startswith('www-') and s.endswith('/')]
        files.sort(cmp=lambda s1,s2:1 if s1<s2 else -1)
        r = run('ls -l www')
        ss=r.split(' -> ')
        if len(ss) !=2:
            print ('ERROR: \'www\' is not a symbol link.')
            return
        current = ss[1]
        print ('Found current symlob link point to:%s\n' % current)
        try:
            index=files.index(current)
        except ValueError,e:
            print ('ERROR: symbol link is invalid.')
            return
        if len(files) == index+1:
            print ('ERROR: already the oldest version.')
        old = files[index+1]
        print ('==================================================')
        for f in files:
            if f == current:
                print ('      Current ---> %s' % current)
            elif f == old:
                print ('  Rollback to ---> %s' % old)
            else:
                print ('                   %s' % f)
        print ('==================================================')
        print ('')
        yn = raw_input('continue? y/N ')
        if yn != 'y' and yn != 'Y':
            print ('Rollback cancelled.')
            return
        print ('Start rollback...')
        sudo('rm -f www')
        sudo('ln -s %s www' % old)
        sudo('chown www:www www')
        # 重启服务后台API
        with settings(warn_only=False):
            for index in xrange(len(API_SERVER)):
                run('supervisorctl restart %s' % API_SERVER[index])
        print ('ROLLBACKED OK.')

@roles('deploysvr')
def rollbacksvr():
    """
    回退到一个版本
    :return:
    """
    with cd(REMOTE_BASE_DIR):
        r = run('ls -p -l')
        files = [s[:-1] for s in RE_FILES.split(r) if s.startswith('www-') and s.endswith('/')]
        files.sort(cmp=lambda s1, s2: 1 if s1 < s2 else -1)
        r = run('ls -l www-dev')
        ss = r.split(' -> ')
        if len(ss) != 2:
            print ('ERROR: \'www-dev\' is not a symbol link.')
            return
        current = ss[1]
        print ('Found current symlob link point to:%s\n' % current)
        try:
            index = files.index(current)
        except ValueError, e:
            print ('ERROR: symbol link is invalid.')
            return
        if len(files) == index + 1:
            print ('ERROR: already the oldest version.')
        old = files[index + 1]
        print ('==================================================')
        for f in files:
            if f == current:
                print ('      Current ---> %s' % current)
            elif f == old:
                print ('  Rollback to ---> %s' % old)
            else:
                print ('                   %s' % f)
        print ('==================================================')
        print ('')
        yn = raw_input('continue? y/N ')
        if yn != 'y' and yn != 'Y':
            print ('Rollback cancelled.')
            return
        print ('Start rollback...')
        sudo('rm -f www-dev',user=sudouser)
        sudo('ln -s %s www-dev' % old,user=sudouser)
        sudo('chown www:www www-dev')
        # 重启服务后台API
        with settings(warn_only=False):
            for index in xrange(len(API_SERVER)):
                sudo('supervisorctl restart %s' % API_SERVER[index],user=sudouser)
        print ('ROLLBACKED OK.')


#定义一个TestServer部署任务
@roles('testsvr')
def deploytest():
    'TestServer部署任务'
    #远程服务器的临时文件
    global remote_dist_dir
    remote_tmp_tar = '/tmp/www-svr.tar.gz'
    remote_dist_link = '/var/www'
    tag = datetime.now().strftime('%y.%m.%d.%H.%M.%S')
    if not remote_dist_link:
        run('touch %s',remote_dist_link)
    # delete old remote_dir
    if remote_dist_dir and remote_dist_dir != '':
        run('rm -rf %s' % remote_dist_dir)
    run('rm -f %s' % remote_tmp_tar)
    with settings(warn_only=True):
        print '可能无临时目录...'
    # 解压:
    remote_dist_dir = '/var/www-svr@%s' % tag
    put('www-svr.tar.gz', remote_tmp_tar)
    run('mkdir %s' % remote_dist_dir)
    with cd(remote_dist_dir):
        run('tar -xzvf %s' % remote_tmp_tar)
        # 设置新目录的www权限
    run('chown -R www:www %s' % remote_dist_dir)
    # 删除旧的连接
    run('rm -f %s' % remote_dist_link)
    # 创建新的软链接指向新部署的目录：
    run('ln -s %s %s' % (remote_dist_dir, remote_dist_link))
    run('chown -R www:www %s' % remote_dist_link)

    #重启服务后台API
    with settings(warn_only=False):
        for index in xrange(len(API_SERVER)):
              run('supervisorctl restart %s' % API_SERVER[index])

###################################
##不直接写明文到程序文件中，通过ssh认证方式
#取得开发本地sshkey
# @roles('developer')
# def get_sshkey():
#     get('/root/.ssh/id_rsa.pub', 'id_rsa.pub.master')
#
# #上传sshkey到嗠器
# @roles('deployserver')
# def put_sshkey():
#     with cd('/tmp'):
#         put('id_rsa.pub.master', 'id_rsa.pub.master')
#         run('cat id_rsa.pub.master >> /root/.ssh/authorized_keys')
####################################
#定义一个DeployServer部署任务
@roles('deploysvr')
def deploysvr():
    'DeployServer部署任务'
    global remote_sdist_dir
    remote_tmp_tar = '/tmp/www-svr.tar.gz'
    remote_dist_link = '/var/www_dev'
    if not remote_dist_link:
        sudo('touch %s',remote_dist_link)
    tag = datetime.now().strftime('%y.%m.%d.%H.%M.%S')
    # delete old remote_dir
    if remote_sdist_dir and remote_sdist_dir != '':
        sudo('rm -rf %s' % remote_sdist_dir,user=sudouser)
    sudo('rm -f %s' % remote_tmp_tar,user=sudouser)
    with settings(warn_only=True):
        print '可能无临时目录...'
    # 解压:
    remote_sdist_dir = '/var/www-svr@%s' % tag
    put('www-svr.tar.gz', remote_tmp_tar)
    sudo('mkdir %s' % remote_sdist_dir,user=sudouser)
    with cd(remote_sdist_dir):
        sudo('tar -xzvf %s' % remote_tmp_tar,user=sudouser)
        # 设置新目录的www权限
    sudo('chown -R www:www %s' % remote_sdist_dir,user=sudouser)
    # 删除旧的连接
    sudo('rm -f %s' % remote_dist_link,user=sudouser)
    # 创建新的软链接指向新部署的目录：
    sudo('ln -s %s %s' % (remote_sdist_dir, remote_dist_link),user=sudouser)
    sudo('chown -R www:www %s' % remote_dist_link,user=sudouser)

    # 重启服务后台API
    # with settings(warn_only=False):
    #     for index in xrange(len(api_server)):
    #         sudo('supervisorctl restart %s' % api_server[index],user=sudouser)


def deploy():
    execute(deploytest)
    #exec()
#错误处理


# def do_ssj_conn():
#     execute(get_sshkey)
#     execute(put_sshkey)

def test():
    pass
